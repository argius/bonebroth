#!/bin/sh

# Installer

set -eu

prodname=bonebroth
ver=1.0.0-beta2
owner=argius
execname=$prodname
execdir=/usr/local/bin

baseurl=https://github.com/$owner/$prodname
zipfile=${prodname}-${ver}-bin.zip
zipurl=$baseurl/releases/download/v$ver/$zipfile
jarfile=${prodname}-${ver}.jar
execfile=$execdir/$execname
javaopts=""

current_script_expr="\$0"

errexit() {
  printf "\033[31m[ERROR]\033[0m" ; echo " $1"
  echo "Installation incomplete."
  exit 1
}

onexit() {
  if [ -n "$tmpdir" ]; then
    cd $tmpdir/..
    test -f $tmpdir/$jarfile && rm $tmpdir/$jarfile
    test -f $tmpdir/$zipfile && rm $tmpdir/$zipfile
    test -f $tmpdir/boot.sh  && rm $tmpdir/boot.sh
    rmdir $tmpdir
  fi
}

tmpdir=`mktemp -d /tmp/${prodname}-XXXXXX`
trap onexit EXIT
trap "trap - EXIT; onexit; exit -1" 1 2 15 # SIGHUP SIGINT SIGTERM

echo "This is the installer of \"$prodname\"."
echo ""

# OS specific settings
case "`uname -a`" in
  Linux* )
    echo "adjusting for Linux"
    if [ -d ~/.local/bin ] && [ -w ~/.local/bin ]; then
      execdir=~/.local/bin
    elif [ -d ~/bin ] && [ -w ~/bin ]; then
      execdir=~/bin
    elif [ -d /usr/local/bin ] && [ -w /usr/local/bin ]; then
      execdir=/usr/local/bin
    else
      errexit "cannot detect writable exec dir, requires ~/.local/bin or ~/bin"
    fi
    execfile=$execdir/$execname
    echo ""
    ;;
  *BSD* )
    echo "adjusting for *BSD"
    if [ -d ~/bin ] && [ -w ~/bin ]; then
      execdir=~/bin
    elif [ -d /usr/local/bin ] && [ -w /usr/local/bin ]; then
      execdir=/usr/local/bin
    else
      errexit "cannot detect writable exec dir, requires ~/bin"
    fi
    execfile=$execdir/$execname
    echo ""
    ;;
  CYGWIN* )
    echo "adjusting for Cygwin"
    current_script_expr="\`cygpath -m \$0\`"
    echo ""
    ;;
esac

echo "installing version $ver into $execdir,"
echo "and uses $tmpdir as a working directory."
cd $tmpdir || errexit "failed to change directory"
echo "downloading: $zipurl"
curl -fsSLO $zipurl || errexit "failed to download zip"
unzip -o $zipfile $jarfile || errexit "failed to unzip"

{
    echo "#!/bin/sh"
    echo "JAVA_OPTS=\"\$JAVA_OPTS $javaopts\""
    echo "java \$JAVA_OPTS -jar \"${current_script_expr}\" \"\$@\""
    echo "exit $?"
    echo ""
} > boot.sh
cat boot.sh $jarfile > $execfile
test -f $execfile || errexit "failed to create execfile"
chmod +x $execfile || errexit "failed to change a permission"

echo "\"$prodname\" has been installed to $execfile ."
echo "checking installation => `$execname --version 2>&1`"
test -n "`$execname --version 2>&1`" || errexit "failed to check installation"
echo ""
echo "Installation completed."

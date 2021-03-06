BoneBroth
=========
[![Build Status](https://travis-ci.org/argius/bonebroth.png)](https://travis-ci.org/argius/bonebroth)

BoneBroth is a text generator using Apache Velocity and some config libraries.

BoneBroth requires JRE 8 or later.

Supported config libraries are as follows:

- YAML (SnakeYAML)
- TypeSafeConfig
- CSV and TSV (Apache Commons CSV + custom format)
- Properties (java.util.Properties)

See [the project's wiki page](https://github.com/argius/bonebroth/wiki) for further information.



To Install
----------

Run the following command.

```sh
$ curl -fsSL http://bit.ly/instbonebroth | sh
```

or

```sh
$ curl -fsSL https://goo.gl/N7PttL | sh
```

Both of these urls are shortened of `https://raw.githubusercontent.com/argius/bonebroth/master/install.sh`.

To uninstall, remove `$(which bonebroth)`.


You need only to download, see [the releases page](https://github.com/argius/bonebroth/releases).



Usage
-----

Run the following command.

```
$ bonebroth --help
```



Examples
--------

### Input Files

 * `example.vm`

```
/*
 * This code has been generated by $generator.
 */
package $package;

/**
 * ${classDescription}.
 *
 * @author $author.name
 */
public final class $className {

    /*
     * comment:
     * numbers =#foreach( $i in $a.numbers )#set( $j=$i+1 ) [$j]#end

     */

#foreach( $item in $items )
    private $item.type $item.id.camel;
#end

    public $className() {
#foreach( $item in $items )
        this.$item.id.camel = $item.value;
#end
    }
#foreach( $o in $items )
  #set( $id = $o.id )

    /**
     * $m.message("template.getter", $o.name)
     * @return $o.name
     */
    public $o.type get${id.pascal}() {
        return $id.camel;
    }

    /**
     * $m.message("template.setter", $o.name)
     * @param $id.camel value to set
     */
    public void set${id.pascal}($o.type $id.camel) {
        this.$id.camel = $id.camel;
    }
#end

}
```

 * `example.csv`

```
@className,Example
@package,bonebroth
@classDescription,Example of BoneBroth
# comment
# id, type, name, value
EXAMPLE_MESSAGE,String,example message,"""hello"""
VALUE_NUMBER,int,a number of value,3
END_OF_DATA,boolean,end of data,false
```


 * `example.conf` (TypeSafeConfig)

```

a.numbers=[2,3,5]

author : {
  name : "argius"
}
```

### Command to run and Result

 * normal mode

```
$ bonebroth -t example.vm -i example.csv,example.conf
/*
 * This code has been generated by BoneBroth version 1.1.0 [build #25 on 2018-02-02T11:20:38Z].
 */
package bonebroth;

/**
 * Example of BoneBroth.
 *
 * @author argius
 */
public final class Example {

    /*
     * comment:
     * numbers = [3] [4] [6]
     */

    private String exampleMessage;
    private int valueNumber;
    private boolean endOfData;

    public Example() {
        this.exampleMessage = "hello";
        this.valueNumber = 3;
        this.endOfData = false;
    }

    /**
     * Gets example message.
     * @return example message
     */
    public String getExampleMessage() {
        return exampleMessage;
    }

    /**
     * Sets example message.
     * @param exampleMessage value to set
     */
    public void setExampleMessage(String exampleMessage) {
        this.exampleMessage = exampleMessage;
    }

    /**
     * Gets a number of value.
     * @return a number of value
     */
    public int getValueNumber() {
        return valueNumber;
    }

    /**
     * Sets a number of value.
     * @param valueNumber value to set
     */
    public void setValueNumber(int valueNumber) {
        this.valueNumber = valueNumber;
    }

    /**
     * Gets end of data.
     * @return end of data
     */
    public boolean getEndOfData() {
        return endOfData;
    }

    /**
     * Sets end of data.
     * @param endOfData value to set
     */
    public void setEndOfData(boolean endOfData) {
        this.endOfData = endOfData;
    }

}
```

 * bean mode

```
$ bonebroth --bean -i example.csv
package bonebroth;

/**
 * Example of BoneBroth.
 */
public final class Example {

    /** example message */
    private String exampleMessage;

    /** a number of value */
    private int valueNumber;

    /** end of data */
    private boolean endOfData;

    /**
     * .
     */
    public Example() {
        // empty
    }

    /**
     * Gets example message.
     * @return example message
     */
    public String getExampleMessage() {
        return exampleMessage;
    }

    /**
     * Sets example message.
     * @param exampleMessage value to set
     */
    public void setExampleMessage(String exampleMessage) {
        this.exampleMessage = exampleMessage;
    }

    /**
     * Gets a number of value.
     * @return a number of value
     */
    public int getValueNumber() {
        return valueNumber;
    }

    /**
     * Sets a number of value.
     * @param valueNumber value to set
     */
    public void setValueNumber(int valueNumber) {
        this.valueNumber = valueNumber;
    }

    /**
     * Gets end of data.
     * @return end of data
     */
    public boolean isEndOfData() {
        return endOfData;
    }

    /**
     * Sets end of data.
     * @param endOfData value to set
     */
    public void setEndOfData(boolean endOfData) {
        this.endOfData = endOfData;
    }
}
```

TIPS
----

### Running BoneBroth without input files

Set a comma alone to the `--input` option.

```
$ bonebroth -t $0 -i ,
```


### Running BoneBroth like a script

- `generator-script`

```
#*
/usr/bin/env bonebroth -t $0 -i ,
exit $?; *###
$util.enrich("BoneBroth").chain
```

- Running this script

```
$ chmod +x generator-script
$ ./generator-script
bone-broth
```


License
-------

Apache 2.0 License.

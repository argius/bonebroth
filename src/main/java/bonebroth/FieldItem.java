package bonebroth;

/**
 * Field item of records in CSV or TSV.
 */
public final class FieldItem {

    private RichValue id;
    private String type;
    private String name;
    private String value;

    public FieldItem() {
    }

    public RichValue getId() {
        return id;
    }

    public void setId(RichValue id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

package agentarium;

public class ModelElementReference {
    private String name = null;
    private ModelElementAccessor modelElementAccessor = null;

    public void setName(String name) {
        if (this.name == null)
            this.name = name;
    }

    public void setModelElementAccessor(ModelElementAccessor modelElementAccessor) {
        if (this.modelElementAccessor == null)
            this.modelElementAccessor = modelElementAccessor;
    }

    public String getName() {
        return name;
    }

    public ModelElementAccessor getModelElementAccessor() {
        return modelElementAccessor;
    }
}

package cn.replux.modelmaker.pojo;
import com.sun.tools.javac.util.Name;

import java.util.List;
import java.util.Map;

public class ModelDefinition {

    private String name;
    private String outputPath;
    private List<String> characteristics;
    private Map<String,String> fieldDecls;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public List<String> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(List<String> characteristics) {
        this.characteristics = characteristics;
    }

    public Map<String, String> getFieldDecls() {
        return fieldDecls;
    }

    public void setFieldDecls(Map<String, String> fieldDecls) {
        this.fieldDecls = fieldDecls;
    }
}

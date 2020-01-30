package cn.replux.modelmaker.processor;

import com.squareup.javapoet.FieldSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class JavaPoetUtil {

    static final Logger logger = LogManager.getLogger(JavaPoetUtil.class);

    static ArrayList<FieldSpec> createFieldSepcs(Map<String, String> fieldDecls) {
        ArrayList<FieldSpec> fieldSpecs = new ArrayList<>();
        fieldDecls.forEach((name,type)->{
            Optional.ofNullable(CommonUtil.getClz(type))
                    .ifPresent(clz->{
                        FieldSpec fieldSpec = FieldSpec.builder(clz, name)
                                .addModifiers(Modifier.PRIVATE)
                                .build();
                        fieldSpecs.add(fieldSpec);
                    });
        });
        return fieldSpecs;
    }


}

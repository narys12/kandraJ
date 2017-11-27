package com.kandra.kandraJ;

import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;

public class BasicGenerator {

    public static final String TITLE = "TITLE";

    public static JavaFile generateTitle(String site, String information, String selector, String attribute) {
        ClassName parser = ClassName.get("com.workit.crawl.parsing.api.common", "Parser");
        ClassName element = ClassName.get("org.jsoup.nodes", "Element");
        ClassName functions = ClassName.get("com.google.common.base", "Functions");
        ClassName jSoupElements = ClassName.get("com.workit.crawl.parsing.api.jsoup", "JSoupElements");
        TypeName implParser = ParameterizedTypeName.get(parser, WildcardTypeName.get(String.class));

        FieldSpec infoSelector = FieldSpec.builder(String.class, information + "_SELECTOR")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer("$S", selector)
                .build();

        MethodSpec.Builder apply = MethodSpec.methodBuilder("apply")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addAnnotation(Override.class)
                .addParameter(element, "element")
                .addStatement("return compose(fromElementText(), toElement()).apply(element)");
        if (StringUtils.isBlank(attribute))
            apply.addStatement("return compose(fromElementText(), toElement()).apply(element)");
        else
            apply.addStatement("return compose(fromAttribute($S), toElement()).apply(element)", attribute);

        TypeSpec titleParser = TypeSpec.classBuilder("TitleParser")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(implParser)
                .addMethod(apply.build())
                .addField(infoSelector)
                .build();

        JavaFile javaFile = JavaFile.builder("com.workit.crawl." + site + ".data.specification", titleParser)
                .addStaticImport(functions, "compose")
                .addStaticImport(jSoupElements, "fromElementText", "toElement")
                .build();

        return javaFile;
    }
}

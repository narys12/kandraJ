package com.kandra.kandraJ;

import com.kandra.kandraJ.generator.ImageGenerator;
import com.kandra.kandraJ.generator.SkuGenerator;
import com.kandra.kandraJ.generator.TitleGenerator;
import com.squareup.javapoet.JavaFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
public class ProjectGenerator {

    @RequestMapping("/")
    public String generate() {
        String site = "conrad";
        String directory = "D:/" + site;
        JavaFile titleGenerator = TitleGenerator.generateTitle(site, ".product-view .product-name");
        JavaFile skuGenerator = SkuGenerator.generaSku(site, ".product-view .product-name");
        JavaFile generaImage = ImageGenerator.generaImage(site, ".product-view .product-name", "src");
        try {
            titleGenerator.writeTo(new File(directory));
            skuGenerator.writeTo(new File(directory));
            generaImage.writeTo(new File(directory));
        } catch (IOException e) {
            return "Error";
        }
        return "Success";
    }

}

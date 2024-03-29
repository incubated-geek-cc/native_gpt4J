package gpt4all;

import com.hexadevlabs.gpt4all.LLModel;
import java.io.File;
import java.nio.file.Paths;

public class ChatApplication {
    private LLModel model;
    LLModel.GenerationConfig config;
    
    public ChatApplication() {
        String modelFilePath = System.getProperty("user.dir") + File.separator + "external" + File.separator + "ggml-gpt4all-j-v1.3-groovy.bin";
        model = new LLModel(Paths.get(modelFilePath));
        config = LLModel.config().withNPredict(4096).build();
    }
    
    public String getResponse(String prompt) {
        String fullGeneration = model.generate(prompt, config, true);
        char c = 10;
        fullGeneration=fullGeneration.substring(fullGeneration.indexOf(c)+1);
        
        return fullGeneration;
    }
}
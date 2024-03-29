package com.hexadevlabs.gpt4all;

import jnr.ffi.LibraryLoader;
import jnr.ffi.LibraryOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {

    private static final Logger logger = LoggerFactory.getLogger(Util.class);
    private static final CharsetDecoder cs = StandardCharsets.UTF_8.newDecoder();

    public static LLModelLibrary loadSharedLibrary(String librarySearchPath){
        String libraryName = "llmodel";
        Map<LibraryOption, Object> libraryOptions = new HashMap<>();
        libraryOptions.put(LibraryOption.LoadNow, true); // load immediately instead of lazily (ie on first use)
        libraryOptions.put(LibraryOption.IgnoreError, false); // calls shouldn't save last errno after call

        if(librarySearchPath!=null) {
            Map<String, List<String>> searchPaths = new HashMap<>();
            searchPaths.put(libraryName, Arrays.asList(librarySearchPath));

            return LibraryLoader.loadLibrary(LLModelLibrary.class,
                    libraryOptions,
                    searchPaths,
                    libraryName
            );
        } else {
            return LibraryLoader.loadLibrary(LLModelLibrary.class,
                    libraryOptions,
                    libraryName
            );
        }
    }

    public static String getValidUtf8(byte[] bytes) {
        try {
            return cs.decode(ByteBuffer.wrap(bytes)).toString();
        } catch (CharacterCodingException e) {
            return null;
        }
    }
}

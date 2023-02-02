package com.ioannuwu.inline.domain;

import java.util.Arrays;

public interface Filter {

    boolean accept(RangeHighlighterWrapper wrapper);

    class ByIgnoreList implements Filter {

        private final String[] ignoreList;

        public ByIgnoreList(String[] ignoreList) {
            this.ignoreList = Arrays.stream(ignoreList).filter((str -> !str.isBlank())).toArray(String[]::new);
        }

        @Override
        public boolean accept(RangeHighlighterWrapper wrapper) {
            String description = wrapper.getDescription();
            for (String pattern : ignoreList) {
                if (description.contains(pattern)) return false;
            }
            return true;
        }
    }
}
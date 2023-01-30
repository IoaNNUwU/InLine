package com.ioannuwu.inline.domain;

public interface Filter {

    boolean accept(RangeHighlighterWrapper wrapper);

    class ByIgnoreList implements Filter {

        private final String[] ignoreList;

        public ByIgnoreList(String[] ignoreList) {
            this.ignoreList = ignoreList;
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
package com.fruit.foryandex;

public class Artist {
        private String name;
        private String info;
        private String coverResource;

        public Artist(String name, String info,  String cover){
            this.name=name;
            this.info=info;
            this.coverResource=cover;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public  String getCoverResource() {
            return coverResource;
        }

        public void setCoverResource( String coverResource) {
            this.coverResource = coverResource;
        }
    }
package com.example.dnd_manager.updater;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubRelease {
    @JsonProperty("tag_name")
    public String tagName;

    @JsonProperty("assets")
    public List<Asset> assets;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Asset {
        @JsonProperty("browser_download_url")
        public String downloadUrl;
        @JsonProperty("name")
        public String name;
    }
}
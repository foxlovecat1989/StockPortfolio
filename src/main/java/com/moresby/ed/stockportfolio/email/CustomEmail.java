package com.moresby.ed.stockportfolio.email;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomEmail {
    private String from;
    private String to;
    private String title;
    private String contentHtml;
    @Builder
    public CustomEmail(String from, String to, String title, String contentHtml) {
        this.from = from;
        this.to = to;
        this.title = title;
        this.contentHtml = contentHtml;
    }
}

package com.yutsuki.chatserver.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
public class DocumentController {

    @Value("${api.server.name}")
    private String serverName;

    private enum Themes {
        alternate,
        moon,
        purple,
        solarized,
        bluePlanet,
        saturn,
        kepler,
        mars,
        deepSpace,
        none
    }

    private enum Layouts {
        modern,
        classic
    }

    @GetMapping("${springdoc.swagger-ui.path}")
    public String serveHtml() {
        return """
                <!doctype html>
                <html lang="en">
                  <head>
                    <title>${TITLE}</title>
                    <meta charset="utf-8" />
                    <meta
                      name="viewport"
                      content="width=device-width, initial-scale=1" />
                  </head>
                  <body>
                    <script id="api-reference" data-url="/v3/api-docs"></script>
                
                    <script>
                      var configuration = {
                        layout: '${LAYOUT}',
                        theme: '${THEME}',
                        hideDownloadButton: true
                      }
                
                      document.getElementById('api-reference').dataset.configuration =
                        JSON.stringify(configuration)
                    </script>
                
                    <script src="https://cdn.jsdelivr.net/npm/@scalar/api-reference"></script>
                  </body>
                </html>
                """
                .replace("${TITLE}", serverName)
                .replace("${LAYOUT}", Layouts.modern.name())
                .replace("${THEME}", Themes.none.name());
    }
}


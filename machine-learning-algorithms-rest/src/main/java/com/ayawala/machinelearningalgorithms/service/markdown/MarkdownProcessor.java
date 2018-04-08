package com.ayawala.machinelearningalgorithms.service.markdown;

import com.vladsch.flexmark.ast.*;
import com.vladsch.flexmark.ext.abbreviation.AbbreviationExtension;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.definition.DefinitionExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.ext.wikilink.WikiImage;
import com.vladsch.flexmark.ext.wikilink.WikiLink;
import com.vladsch.flexmark.ext.wikilink.WikiLinkExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.LinkResolver;
import com.vladsch.flexmark.html.LinkResolverFactory;
import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import com.vladsch.flexmark.html.renderer.LinkStatus;
import com.vladsch.flexmark.html.renderer.ResolvedLink;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.DataKey;
import com.vladsch.flexmark.util.options.MutableDataHolder;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.error.Mark;

import java.util.Arrays;
import java.util.Set;

@Component
public class MarkdownProcessor {

    public static final DataKey<String> IMAGE_PREFIX = new DataKey<>("IMAGE_PREFIX", "");
    public static final DataKey<String> LINK_PREFIX = new DataKey<>("IMAGE_PREFIX", "");

    public MarkdownResult process(final MarkdownResult result, final MarkdownRequest request) throws MarkdownException {
        final MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, Arrays.asList(StrikethroughExtension.create()));
        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");
        options.set(Parser.FENCED_CODE_BLOCK_PARSER, true);
        options.set(HtmlRenderer.FENCED_CODE_LANGUAGE_CLASS_PREFIX, "language-none");
        options.set(HtmlRenderer.RENDER_HEADER_ID, true);
        options.set(WikiLinkExtension.IMAGE_LINKS, true);
        options.set(WikiLinkExtension.LINK_FIRST_SYNTAX, true);
        options.set(Parser.EXTENSIONS, Arrays.asList(
                AbbreviationExtension.create(), AutolinkExtension.create(), DefinitionExtension.create(),
                TablesExtension.create(), WikiLinkExtension.create(), TocExtension.create(),
                CustomHtmlRendererExtension.create(result)));
        options.set(IMAGE_PREFIX, request.getImagePrefix());
        options.set(LINK_PREFIX, request.getLinkPrefix());

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(request.getMarkdown());
        String html = renderer.render(document);
        result.setHtmlContent(html);

        return result;
    }

    static class CustomHtmlRendererExtension implements HtmlRenderer.HtmlRendererExtension {

        private MarkdownResult result;

        public CustomHtmlRendererExtension(final MarkdownResult result) {
            this.result = result;
        }

        @Override
        public void rendererOptions(final MutableDataHolder options) {}

        @Override
        public void extend(final HtmlRenderer.Builder rendererBuilder, final String rendererType) {
            rendererBuilder.linkResolverFactory(new CustomLinkResolver.CustomLinkResolverFactory(result));
        }

        static CustomHtmlRendererExtension create(final MarkdownResult result) {
            return new CustomHtmlRendererExtension(result);
        }
    }

    static class CustomLinkResolver implements LinkResolver {

        private MarkdownResult result;

        public CustomLinkResolver(final MarkdownResult result) {
            this.result = result;
        }

        @Override
        public ResolvedLink resolveLink(Node node, LinkResolverContext linkResolverContext, ResolvedLink resolvedLink) {
            if(node instanceof Image || node instanceof ImageRef || node instanceof WikiImage) {
                String url = resolvedLink.getUrl();
                if(!url.startsWith("http:") && !url.startsWith("https:")) {
                    result.getImages().add(url);
                    url = prefixUrl(url, linkResolverContext.getOptions().get(IMAGE_PREFIX));
                }
                return resolvedLink.withStatus(LinkStatus.VALID).withUrl(url);
            } else if(node instanceof WikiLink || node instanceof Link || node instanceof LinkRef) {
                String url = resolvedLink.getUrl();
                if(!url.startsWith("http:") && !url.startsWith("https:")) {
                    url = prefixUrl(url, linkResolverContext.getOptions().get(LINK_PREFIX));
                }
                return resolvedLink.withStatus(LinkStatus.VALID).withUrl(url);
            }
            return resolvedLink;
        }

        private static String prefixUrl(final String url, final String prefix) {
            return (prefix == null ? "" : prefix) + url;
        }

        static class CustomLinkResolverFactory implements LinkResolverFactory {

            private MarkdownResult result;

            public CustomLinkResolverFactory(final MarkdownResult result) {
                this.result = result;
            }

            @Override
            public Set<Class<? extends LinkResolverFactory>> getAfterDependents() {
                return null;
            }

            @Override
            public Set<Class<? extends LinkResolverFactory>> getBeforeDependents() {
                return null;
            }

            @Override
            public boolean affectsGlobalScope() {
                return false;
            }

            @Override
            public LinkResolver create(LinkResolverContext linkResolverContext) {
                return new CustomLinkResolver(result);
            }
        }
    }
}

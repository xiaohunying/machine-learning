package com.ayawala.machinelearningalgorithms.service.io;

import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class ContentRepositoryFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentRepositoryFactory.class);

    public ContentRepository build(final String path) {
        final ContentRepository contentRepository = new ContentRepository();
        if(path.startsWith("embedded")) {
            buildFromEmbedded(contentRepository, path);
        } else if(path.startsWith("http")) {
            buildFromUrl(contentRepository, path);
        }
        return contentRepository;
    }

    private ContentRepository buildFromUrl(final ContentRepository contentRepository, final String url) {
        ByteSource byteSource;
        try {
            byteSource  = Resources.asByteSource(new URL(url));

            try (final InputStream inputStream = byteSource.openStream();
                 final ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
                ZipEntry zipEntry = zipInputStream.getNextEntry();
                while(zipEntry != null) {
                    if(!zipEntry.isDirectory()){
                        final String name = zipEntry.getName();
                        final byte[] bytes = ByteStreams.toByteArray(zipInputStream);
                        contentRepository.getSource().put(name, new ContentNode(name, bytes.length, ByteSource.wrap(bytes)));
                    }
                    zipEntry = zipInputStream.getNextEntry();
                }
            }
        } catch (MalformedURLException e) {
            LOGGER.warn(String.format("Failed to stream content from [$s]!", url), e);
        } catch (IOException e) {
            LOGGER.warn(String.format("Failed to stream content from [$s]!", url), e);
        }
        return contentRepository;
    }

    private ContentRepository buildFromEmbedded(final ContentRepository contentRepository, final String path) {
        final ByteSource byteSource = Resources.asByteSource(Resources.getResource(path));
        try (final InputStream inputStream = byteSource.openStream();
             final ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while(zipEntry != null) {
                if(!zipEntry.isDirectory()){
                    final String name = zipEntry.getName();
                    final byte[] bytes = ByteStreams.toByteArray(zipInputStream);
                    contentRepository.getSource().put(name, new ContentNode(name, bytes.length, ByteSource.wrap(bytes)));
                }
                zipEntry = zipInputStream.getNextEntry();
            }
        } catch (IOException e) {
            LOGGER.warn(String.format("Failed to stream content from [$s]!", path), e);
        }
        return contentRepository;
    }
}

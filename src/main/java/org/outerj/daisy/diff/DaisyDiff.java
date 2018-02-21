/*
 * Copyright 2004 Guy Van den Broeck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.outerj.daisy.diff;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.outerj.daisy.diff.html.HTMLDiffer;
import org.outerj.daisy.diff.html.HtmlSaxDiffOutput;
import org.outerj.daisy.diff.html.TextNodeComparator;
import org.outerj.daisy.diff.html.dom.DomTreeBuilder;
import org.outerj.daisy.diff.tag.TagComparator;
import org.outerj.daisy.diff.tag.TagDiffer;
import org.outerj.daisy.diff.tag.TagSaxDiffOutput;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

public class DaisyDiff {

    /**
     * Diffs two html files, outputting the result to the specified consumer.
     */
    public static void diffHTML(InputSource oldSource, InputSource newSource,
            ContentHandler consumer, String prefix, Locale locale)
            throws SAXException, IOException {

        DomTreeBuilder oldHandler = new DomTreeBuilder();
        XMLReader xr1 = XMLReaderFactory.createXMLReader();
        xr1.setContentHandler(oldHandler);
        xr1.parse(oldSource);
        TextNodeComparator leftComparator = new TextNodeComparator(oldHandler,
                locale);

        DomTreeBuilder newHandler = new DomTreeBuilder();
        XMLReader xr2 = XMLReaderFactory.createXMLReader();
        xr2.setContentHandler(newHandler);
        xr2.parse(newSource);

        TextNodeComparator rightComparator = new TextNodeComparator(newHandler,
                locale);

        HtmlSaxDiffOutput output = new HtmlSaxDiffOutput(consumer, prefix);
        HTMLDiffer differ = new HTMLDiffer(output);
        differ.diff(leftComparator, rightComparator);
    }

    /**
     *  Diffs two HTML files, outputting a diff to the provided output File as HTML with default styles imported
     * @param oldSource first files to compare
     * @param newSource second file to compare
     * @param outputFile a File to which output will be written
     * @param xslPath path to XSL for transforming diff to HTML (header and styles) if null or empty -- generates simple HTML diff with default styles
     * @throws TransformerConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static void diffHtmlSimple(InputSource oldSource, InputSource newSource, File outputFile, String xslPath) throws TransformerConfigurationException, IOException, SAXException {
        final SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();

        final TransformerHandler res = tf.newTransformerHandler();
        res.getTransformer().setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        res.getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");
        res.getTransformer().setOutputProperty(OutputKeys.METHOD, "html");
        res.getTransformer().setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        res.setResult(new StreamResult(outputFile));

        final XslFilter filter = new XslFilter();

        if (xslPath == null || xslPath.trim().isEmpty()) {
            xslPath = "xslfilter/simplehtmlwithcss.xsl";
        }

        ContentHandler postProcess = filter.xsl(res, xslPath);

        Locale locale = Locale.getDefault();
        String prefix = "diff";

        HtmlCleaner cleaner = new HtmlCleaner();

        DomTreeBuilder oldHandler = new DomTreeBuilder();
        cleaner.cleanAndParse(oldSource, oldHandler);
        TextNodeComparator leftComparator = new TextNodeComparator(oldHandler, locale);

        DomTreeBuilder newHandler = new DomTreeBuilder();
        cleaner.cleanAndParse(newSource, newHandler);
        TextNodeComparator rightComparator = new TextNodeComparator(newHandler, locale);

        postProcess.startDocument();

        postProcess.startElement("", "diffreport", "diffreport", new AttributesImpl());
        postProcess.startElement("", "diff", "diff", new AttributesImpl());

        HtmlSaxDiffOutput output = new HtmlSaxDiffOutput(postProcess, prefix);
        HTMLDiffer differ = new HTMLDiffer(output);

        differ.diff(leftComparator, rightComparator);

        postProcess.endElement("", "diff", "diff");
        postProcess.endElement("", "diffreport", "diffreport");

        postProcess.endDocument();
    }

    /**
     * Diffs two html files word for word as source, outputting the result to
     * the specified consumer.
     */
    public static void diffTag(String oldText, String newText,
            ContentHandler consumer) throws Exception {
        consumer.startDocument();
        TagComparator oldComp = new TagComparator(oldText);
        TagComparator newComp = new TagComparator(newText);

        TagSaxDiffOutput output = new TagSaxDiffOutput(consumer);
        TagDiffer differ = new TagDiffer(output);
        differ.diff(oldComp, newComp);
        consumer.endDocument();
    }

    /**
     * Diffs two html files word for word as source, outputting the result to
     * the specified consumer.
     */
    public static void diffTag(BufferedReader oldText, BufferedReader newText,
            ContentHandler consumer) throws Exception {

        TagComparator oldComp = new TagComparator(oldText);
        TagComparator newComp = new TagComparator(newText);

        TagSaxDiffOutput output = new TagSaxDiffOutput(consumer);
        TagDiffer differ = new TagDiffer(output);
        differ.diff(oldComp, newComp);
    }

}

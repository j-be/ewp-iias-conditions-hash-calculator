package it.unibo.ewp.api.iia.get;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ConditionsHashCalculatorRelativeXPathImpl implements ConditionsHashCalculator {

    private final String cooperationConditionExpression;

    public ConditionsHashCalculatorRelativeXPathImpl(String cooperationConditionExpression) {
        this.cooperationConditionExpression = cooperationConditionExpression;
    }

    @Override
    public List<String> calculate(byte[] content) {
        List<String> hashes = new ArrayList<>();

        // parse input document
        Document document = parseContent(content);

        // select all iia elements
        String iiasXpathExpression = "//*[local-name()='iia']";
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        NodeList iiaNodes = null;
        XPathExpression xPathExpression = null;
        try {
            xPathExpression = xPath.compile(iiasXpathExpression);
            iiaNodes = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new ConditionsHashCalculatorException("unable to calculate hash: internal error while selecting iia nodes through XPath: " + e.getMessage(), e);
        }

        // for each iia element calculate the condition hash
        if (iiaNodes != null) {
            for (int i = 0; i < iiaNodes.getLength(); i++) {
                String result;
                try {
                    XPath cooperationConditionXPath = xPathFactory.newXPath();
                    XPathExpression cooperationConditionCompiledExpression = cooperationConditionXPath.compile(cooperationConditionExpression);
                    NodeList nodeList = (NodeList) cooperationConditionCompiledExpression.evaluate(iiaNodes.item(i), XPathConstants.NODESET);
                    byte[] canonicalizedContent = canonicalize(nodeList);
                    result = DigestUtils.sha256Hex(canonicalizedContent);
                } catch (XPathExpressionException e) {
                    throw new ConditionsHashCalculatorException("Error while compiling or evaluating XPath expression " + cooperationConditionExpression + ": " + e.getMessage(), e);
                }
                hashes.add(result);
            }
        }
        return hashes;
    }

    private Document parseContent(byte[] content) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ConditionsHashCalculatorException("unable to calculate hash: error while creating document builder: " + e.getMessage(), e);
        }
        Document document = null;
        try {
            document = documentBuilder.parse(new ByteArrayInputStream(content));
        } catch (SAXException | IOException e) {
            throw new ConditionsHashCalculatorException("unable to calculate hash: error while parsing IIA document: " + e.getMessage(), e);
        }
        return document;
    }

    private byte[] canonicalize(NodeList nodeList) {
        org.apache.xml.security.Init.init();
        Canonicalizer canonicalizer = null;
        try {
            canonicalizer = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
        } catch (InvalidCanonicalizerException e) {
            throw new ConditionsHashCalculatorException("unable to calculate hash: error while instantiating a canonicalizer to canonicalize the cooperataion-conditions element: " + e.getMessage(), e);
        }

        ByteArrayOutputStream canonicalizerOutputStream = new ByteArrayOutputStream();
        try {
            Set<Node> nodeSet = new HashSet<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                nodeSet.add(nodeList.item(i));
            }
            canonicalizer.canonicalizeXPathNodeSet(nodeSet, canonicalizerOutputStream);
        } catch (CanonicalizationException e) {
            throw new ConditionsHashCalculatorException("unable to calculate hash: error while canonicalizing the cooperation-conditions element: " + e.getMessage(), e);
        }
        byte[] canonicalizedContent = canonicalizerOutputStream.toByteArray();
        return canonicalizedContent;
    }

}

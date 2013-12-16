/*
 * Copyright 2003-2013 MarkLogic Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.marklogic.mapreduce;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import com.marklogic.tree.ExpandedTree;
import com.marklogic.tree.NodeKind;

/**
 * MarkLogicDocument retrieved from a MarkLogic forest through Direct Access
 * 
 * @author jchen
 */
public abstract class ForestDocument implements MarkLogicDocument {
    public static final Log LOG = LogFactory.getLog(
            ForestDocument.class);
    
    private long fragmentOrdinal;
    private String[] collections;
    
    public static ForestDocument createDocument(Configuration conf,
            Path forestDir, ExpandedTree tree, String uri) {
        byte rootNodeKind = tree.rootNodeKind();
        ForestDocument doc = null;
        switch (rootNodeKind) {
            case NodeKind.BINARY:
                if (tree.binaryData == null) {
                    doc = new LargeBinaryDocument(conf, forestDir, tree);
                } else {
                    doc = new RegularBinaryDocument(tree);
                }
                break;
            case NodeKind.ELEM:
            case NodeKind.TEXT:
                doc = new DOMDocument(tree);  
                break;
            default:
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Skipping unsupported node kind "
                            + rootNodeKind + " (" + uri + ")");
                }
                return null;
        }
        doc.setFragmentOrdinal(tree.getFragmentOrdinal());
        doc.setCollections(tree.getCollections());
        return doc;
    }

    public long getFragmentOrdinal() {
        return fragmentOrdinal;
    }
    
    private void setFragmentOrdinal(long fragOrdinal) {
        fragmentOrdinal = fragOrdinal; 
    }
    
    public String[] getCollections() {
        return collections;
    }
    
    private void setCollections(String[] cols) {
        collections = cols;
    }
}
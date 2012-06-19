/*
 * Copyright 2012 James Iry Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package com.pogofish.jadt.sink;

import java.io.*;

import com.pogofish.jadt.util.IOExceptionAction;


/**
 * Sink that writes to a specified file in UTF-8
 *
 * @author jiry
 */
public class FileSink implements Sink {
    private final Writer writer;
    final File outputFile;

    @Override
	public String getInfo() {
		return outputFile.getAbsolutePath();
	}

	/**
     * Creates a FileSink based on a complete file name
     * 
     * @param outputFileName String full name of the file to be output
     */
    public FileSink(final String outputFileName) {
        super();
        outputFile = new File(outputFileName);

        writer = new IOExceptionAction<Writer>() {
            @Override
            public Writer doAction() throws IOException {
                final File parentDir = outputFile.getParentFile();
                parentDir.mkdirs();

                outputFile.createNewFile();

                return new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8");
            }
        }.execute();
    }

    @Override
    public void write(final String data) {
        new IOExceptionAction<Writer>() {

            @Override
            public Writer doAction() throws IOException {
                writer.write(data);
                return null;
            }}.execute();
    }

    @Override
    public void close() {
        new IOExceptionAction<Writer>() {

            @Override
            public Writer doAction() throws IOException {
                writer.close();
                return null;
            }}.execute();
    }
}

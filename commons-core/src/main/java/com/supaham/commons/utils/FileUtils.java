/* Copyright (c) 2013 dumptruckman
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.supaham.commons.utils;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Utility methods for working with {@link File} instances. This class contains methods such as
 * {@link #getFileContentsAsString(File)}, {@link #writeStringToFile(String, File)}, and more.
 *
 * @since 0.1
 */
public class FileUtils {

  private static final int BUFFER_SIZE = 1024;

  /**
   * Gets the contents of a {@link File} as a {@link String}.
   *
   * @param file File to read.
   *
   * @return contents of the {@code file}. String will be empty in case of any errors.
   *
   * @throws IOException If the file does not exist <br />If the file cannot be read <br />If the
   * file is a directory
   */
  @NotNull
  public static String getFileContentsAsString(@NotNull final File file) throws IOException {
    if (!file.exists()) {
      throw new IOException("File " + file + " does not exist");
    }
    if (!file.canRead()) {
      throw new IOException("Cannot read file " + file);
    }
    if (file.isDirectory()) {
      throw new IOException("File " + file + " is directory");
    }

    Writer writer = new StringWriter();
    try (InputStream is = new FileInputStream(file);
         Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
      int numberOfCharsRead;
      char[] buffer = new char[BUFFER_SIZE];
      while ((numberOfCharsRead = reader.read(buffer)) != -1) {
        writer.write(buffer, 0, numberOfCharsRead);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return writer.toString();
  }

  /**
   * Writes a {@link String} to a {@link File}.
   *
   * @param string String to write
   * @param file file to write to
   *
   * @throws IOException If an I/O error occurs
   */
  public static void writeStringToFile(@NotNull String string,
                                       @NotNull final File file) throws IOException {
    try (OutputStreamWriter out =
             new OutputStreamWriter(new FileOutputStream(file), "UTF-8")) {
      string = string.replaceAll("\n", System.getProperty("line.separator"));
      out.write(string);
    }
  }
}

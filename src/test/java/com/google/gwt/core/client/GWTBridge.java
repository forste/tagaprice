/*
 * I don't know why but this class wasn't in my GWT 2.1.0 distribution...
 */


/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.core.client;

/**
 * When running in hosted mode, acts as a bridge from {@link GWT} into the
 * hosted mode environment.
 */
public abstract class GWTBridge {

	public abstract <T> T create(Class<?> classLiteral);

	public abstract String getVersion();

	public abstract boolean isClient();

	public abstract void log(String message, Throwable e);
}

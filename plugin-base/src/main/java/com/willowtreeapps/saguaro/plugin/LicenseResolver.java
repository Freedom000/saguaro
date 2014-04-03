/*
 * Copyright (C) 2014 WillowTree Apps Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.willowtreeapps.saguaro.plugin;

import java.util.Set;

/**
 * Resolves all dependencies of a project with their corresponding licenses. Even if a dependency does not have any
 * licenses, it should still be returned so that the user can be notified that they should add the license manually.
 */
public interface LicenseResolver {
    public Set<LicenseDependency> resolveLicenseDependencies() throws PluginException;
}

/**
 * Copyright © 2016-2021 The Thingsboard Authors
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
package org.my.thingsboard.server.msa.connectivity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.thingsboard.rest.client.RestClient;

@Slf4j
public abstract class AbstractContainerTest {
    protected static final String HTTPS_URL = "http://localhost:8080";
//    protected static final String WSS_URL = "wss://localhost";
    protected static String TB_TOKEN;
    protected static RestClient restClient;
    protected ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void before() throws Exception {
        restClient = new RestClient(HTTPS_URL);
//        restClient.getRestTemplate().setRequestFactory(getRequestFactoryForSelfSignedCert());
    }


}

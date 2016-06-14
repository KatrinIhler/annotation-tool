/**
 *  Copyright 2012, Entwine GmbH, Switzerland
 *  Licensed under the Educational Community License, Version 2.0
 *  (the "License"); you may not use this file except in compliance
 *  with the License. You may obtain a copy of the License at
 *
 *  http://www.osedu.org/licenses/ECL-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an "AS IS"
 *  BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *  or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 */
package ch.entwine.annotations.impl;

import static org.opencastproject.util.persistence.PersistenceEnvs.persistenceEnvironment;

import org.opencastproject.security.api.SecurityService;
import org.opencastproject.util.osgi.SimpleServicePublisher;
import org.opencastproject.util.persistence.PersistenceEnv;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;

import java.util.Dictionary;

import javax.persistence.EntityManagerFactory;

import ch.entwine.annotations.api.ExtendedAnnotationService;
import ch.entwine.annotations.impl.persistence.ExtendedAnnotationServiceJpaImpl;

/**
 * Create and register an implementation of {@link ch.entwine.annotations.api.ExtendedAnnotationService} .
 */
public class ExtendedAnnotationServicePublisher extends SimpleServicePublisher {

  private EntityManagerFactory emf;
  private SecurityService securityService;

  /** OSGi DI */
  void setEntityManagerFactory(EntityManagerFactory emf) {
    this.emf = emf;
  }

  /**
   * OSGi callback for setting the security service.
   *
   * @param securityService
   *          the security service
   */
  public void setSecurityService(SecurityService securityService) {
    this.securityService = securityService;
  }

  /**
   * @see org.opencastproject.util.osgi.SimpleServicePublisher#needConfig()
   */
  @Override
  public boolean needConfig() {
    return false;
  }

  /**
   * @see org.opencastproject.util.osgi.SimpleServicePublisher#registerService(Dictionary, ComponentContext)
   */
  @Override
  public ServiceReg registerService(Dictionary properties, ComponentContext cc) throws ConfigurationException {
    final PersistenceEnv penv = persistenceEnvironment(emf);
    final ExtendedAnnotationServiceJpaImpl eas = new ExtendedAnnotationServiceJpaImpl(penv, securityService);
    return ServiceReg.reg(registerService(cc, eas, ExtendedAnnotationService.class, "Extended Annotation Service"));
  }
}

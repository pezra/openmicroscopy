/*
 *   $Id$
 *
 *   Copyright 2010 Glencoe Software, Inc. All rights reserved.
 *   Use is subject to license terms supplied in LICENSE.txt
 *
 */

#ifndef OMERO_API_ILDAP_ICE
#define OMERO_API_ILDAP_ICE

#include <omero/ServicesF.ice>
#include <omero/Collections.ice>

module omero {

    module api {
        /**
         * See <a href="http://hudson.openmicroscopy.org.uk/job/OMERO/javadoc/ome/api/ILdap.html">ILdap.html</a>
         **/
        ["ami", "amd"] interface ILdap extends ServiceInterface
            {
                idempotent ExperimenterList searchAll() throws ServerError;
                idempotent StringSet searchDnInGroups(string attr, string value) throws ServerError;
                idempotent ExperimenterList searchByAttribute(string dn, string attribute, string value) throws ServerError;
                idempotent ExperimenterList searchByAttributes(string dn, StringSet attributes, StringSet values) throws ServerError;
                idempotent omero::model::Experimenter searchByDN(string userdn) throws ServerError;
                idempotent string findDN(string username) throws ServerError;
                idempotent omero::model::Experimenter findExperimenter(string username) throws ServerError;
                idempotent void setDN(omero::RLong experimenterID, string dn) throws ServerError;
                idempotent bool getSetting() throws ServerError;
            };

    };
};

#endif

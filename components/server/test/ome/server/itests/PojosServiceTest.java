/*
 * ome.server.itests.PojosServiceTest
 *
 *   Copyright 2006 University of Dundee. All rights reserved.
 *   Use is subject to license terms supplied in LICENSE.txt
 */
package ome.server.itests;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import ome.api.IPojos;
import ome.conditions.ApiUsageException;
import ome.model.ILink;
import ome.model.annotations.CommentAnnotation;
import ome.model.containers.Dataset;
import ome.model.containers.Project;
import ome.model.core.Image;
import ome.model.core.Pixels;
import ome.testing.OMEData;
import ome.testing.ObjectFactory;
import ome.util.CBlock;
import ome.util.builders.PojoOptions;

import org.testng.annotations.Test;

/**
 * 
 * @author Josh Moore, josh at glencoesoftware.com
 */
public class PojosServiceTest extends AbstractManagedContextTest {

    protected IPojos iPojos;

    protected OMEData data;

    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();
        DataSource dataSource = (DataSource) applicationContext
                .getBean("dataSource");
        data = new OMEData();
        data.setDataSource(dataSource);
        iPojos = factory.getPojosService();
    }

    @Test
    public void test_unannotated_Event_version() throws Exception {
        ILink link = createLinkedCommentAnnotation();
        iPojos.deleteDataObject(link, null);

    }

    @Test(groups = "ticket:318")
    public void testLoadHiearchiesHandlesNullRootNodeIds() throws Exception {
        PojoOptions po;

        try {
            iPojos.loadContainerHierarchy(Project.class, null, null);
            fail("Should throw ApiUsage.");
        } catch (ApiUsageException aue) {
            // ok
        }
        po = new PojoOptions().exp(0L);
        iPojos.loadContainerHierarchy(Project.class, null, po.map());

        po = new PojoOptions().grp(0L);
        iPojos.loadContainerHierarchy(Project.class, null, po.map());

    }

    @Test(groups = "ticket:657")
    public void testAnnotationsStillCounted() throws Exception {
        Dataset d = new Dataset();
        d.setName("ticket:657");
        CommentAnnotation da = new CommentAnnotation();
        da.setTextValue("ticket:657");
        da.setNs("");
        d.linkAnnotation(da);
        Image i = new Image();
        i.setName("ticket:657");
        i.linkDataset(d);
        CommentAnnotation ia = new CommentAnnotation();
        ia.setTextValue("ticket:657");
        ia.setNs("");
        i.linkAnnotation(ia);

        d = iUpdate.saveAndReturnObject(d);

        Set<Image> list = iPojos.getImages(Dataset.class, Collections
                .singleton(d.getId()), null);

        i = list.iterator().next();
        // d = i.linkedDatasetList().get(0);
        // No longer joined

        long self = this.iAdmin.getEventContext().getCurrentUserId();
        // Aren't returned now by getImages
        // assertTrue(d.getAnnotationLinksCountPerOwner() != null);
        // assertTrue(d.getAnnotationLinksCountPerOwner().get(self).equals(1L));
        assertTrue(i.getAnnotationLinksCountPerOwner() != null);
        assertTrue(i.getAnnotationLinksCountPerOwner().get(self).equals(1L));

    }

    @Test(groups = "ticket:651")
    public void testIntervalInPojoOptions() {
        Long userID = factory.getAdminService().getEventContext()
                .getCurrentUserId();
        // create
        Dataset ds = new Dataset();
        ds.setName("ticket:651");
        Image im = new Image();
        im.setName("ticket:651");
        Pixels pi = ObjectFactory.createPixelGraph(null);
        im.addPixels(pi);
        ds.linkImage(im);
        ds = iUpdate.saveAndReturnObject(ds);

        // test
        Timestamp startTime = getDate("before");
        Timestamp endTime = getDate("after");

        PojoOptions options = new PojoOptions();
        options.exp(userID);
        options.startTime(startTime);
        options.endTime(endTime);

        iPojos.getImagesByOptions(options.map());
        iPojos.getImages(Dataset.class, Collections.singleton(ds.getId()),
                options.map());
    }

    @Test(groups = "ticket:1018")
    public void testIntervalPojoMethodsReturnsCounts() {
        Long userID = factory.getAdminService().getEventContext()
                .getCurrentUserId();
        // create
        Dataset ds = new Dataset();
        ds.setName("ticket:1018");
        Image im = new Image();
        im.setName("ticket:1018");
        Pixels pi = ObjectFactory.createPixelGraph(null);
        im.addPixels(pi);
        ds.linkImage(im);
        ds = iUpdate.saveAndReturnObject(ds);

        // test
        Timestamp startTime = getDate("before");
        Timestamp endTime = getDate("after");

        PojoOptions options = new PojoOptions();
        options.exp(userID);
        options.startTime(startTime);
        options.endTime(endTime);

        Set<Image> images = iPojos.getImagesByOptions(options.map());
        Image i = images.iterator().next();
        assertTrue(i.getAnnotationLinksCountPerOwner() != null);

    }

    // ~ Helpers
    // =========================================================================

    private Timestamp getDate(String arg) {
        Calendar cal = Calendar.getInstance();
        cal.setLenient(true);
        cal.setTime(new Date());
        if (arg.equals("after")) {
            cal.add(Calendar.DATE, +1);
        }
        if (arg.equals("before")) {
            cal.add(Calendar.DATE, -1);
        }
        Date yesterday = cal.getTime();
        SimpleDateFormat currentDate = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss");
        Timestamp time = Timestamp.valueOf(currentDate.format(yesterday));
        return (time);
    }

    private ILink createLinkedCommentAnnotation() {
        CommentAnnotation da = new CommentAnnotation();
        Dataset ds = new Dataset();
        Project p = new Project();

        p.setName("uEv");
        p.linkDataset(ds);
        ds.setName("uEv");
        da.setNs("");
        da.setTextValue("uEv");
        ds.linkAnnotation(da);
        ds = iPojos.createDataObject(ds, null);
        return ds.collectAnnotationLinks((CBlock<ILink>) null).iterator()
                .next();
    }

}

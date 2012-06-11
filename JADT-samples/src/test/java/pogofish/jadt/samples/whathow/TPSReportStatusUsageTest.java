/*
Copyright 2012 James Iry

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package pogofish.jadt.samples.whathow;


import static org.junit.Assert.*;
import static pogofish.jadt.samples.whathow.data.TPSReportStatus.*;
import static pogofish.jadt.samples.whathow.data.Manager.*;

import org.junit.Test;

import pogofish.jadt.samples.whathow.TPSReportStatusUsage.StatusNotifier;
/**
 * Test to make sure the sample TPS Report status usage does what it says it does
 * @author jiry
 *
 */
public class TPSReportStatusUsageTest {
    private final TPSReportStatusUsage usage = new TPSReportStatusUsage();

    @Test
    public void testWithVisitor() {
        assertFalse(usage.isApproved(_Denied(_Manager("Foo"))));
        assertFalse(usage.isApproved(_Pending()));
        assertTrue(usage.isApproved(_Approved(_Manager("Foo"))));
    }
    
    @Test
    public void testWithInstanceOf() {
        assertFalse(usage.isApprovedV2(_Denied(_Manager("Foo"))));
        assertFalse(usage.isApprovedV2(_Pending()));
        assertTrue(usage.isApprovedV2(_Approved(_Manager("Foo"))));
    }
    
    @Test
    public void testMessage() {
        assertEquals("Approved by Manager(name = Fred)", usage.message(_Approved(_Manager("Fred"))));
        assertEquals("Denied by Manager(name = Bob)", usage.message(_Denied(_Manager("Bob"))));
        assertEquals("Pending", usage.message(_Pending()));
    }
    
    private static final class Ref<T> {
        public T value;
    }
    
    @Test
    public void testNotify() {
        final Ref<String> result = new Ref<String>();
        
        final StatusNotifier notifier = new StatusNotifier() {            
            @Override
            public void notifyPending() {
                result.value = "pending";                
            }
            
            @Override
            public void notifyDenied() {
                result.value = "denied";                
            }
            
            @Override
            public void notifyApproved() {
                result.value = "approved";                
            }
        };
        
        usage.notify(_Approved(_Manager("Fred")), notifier);
        assertEquals("approved", result.value);
        
        usage.notify(_Denied(_Manager("Fred")), notifier);
        assertEquals("denied", result.value);
        
        usage.notify(_Pending(), notifier);
        assertEquals("pending", result.value);
    }
    
    @Test
    public void testNotifyDenid() {
        final Ref<String> result = new Ref<String>();
        
        final StatusNotifier notifier = new StatusNotifier() {            
            @Override
            public void notifyPending() {
                result.value = "pending";                
            }
            
            @Override
            public void notifyDenied() {
                result.value = "denied";                
            }
            
            @Override
            public void notifyApproved() {
                result.value = "approved";                
            }
        };
        
        usage.notifyDenied(_Approved(_Manager("Fred")), notifier);
        assertNull(result.value);
        
        usage.notifyDenied(_Denied(_Manager("Fred")), notifier);
        assertEquals("denied", result.value);
        result.value = null;
        
        usage.notifyDenied(_Pending(), notifier);
        assertNull(result.value);
    }
}

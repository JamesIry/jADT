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
package com.pogofish.jadt.samples.whathow;

import com.pogofish.jadt.samples.whathow.data.TPSReportStatus;
import com.pogofish.jadt.samples.whathow.data.TPSReportStatus.*;

/**
 * Example usage of TPSReportStatus for the how page
 * It is marked up with START SNIPPET and END SNIPPET boundaries to support
 * /JADT/src/site/apt/*.apt
 */
public class TPSReportStatusUsage {
    /**
     * Get approval status of TPSReport
     */
    // START SNIPPET: isApproved
    public boolean isApproved(TPSReportStatus status) {
        return status.accept(new TPSReportStatus.VisitorWithDefault<Boolean>() {
            @Override
            public Boolean visit(Approved x) {
                return true;
            }

            @Override
            protected Boolean getDefault(TPSReportStatus x) {
                return false;
            }
        });
    }
    // END SNIPPET: isApproved
    
    // START SNIPPET: message
    public String message(TPSReportStatus status) {
        return status.accept(new TPSReportStatus.Visitor<String>() {
            @Override
            public String visit(Pending x) {
                return "Pending";
            }

            @Override
            public String visit(Approved x) {
                return "Approved by " + x.approver;
            }

            @Override
            public String visit(Denied x) {
                return "Denied by " + x.rejector;
            }
        });
    }
    // END SNIPPET: message
    
    // START SNIPPET: notify
    public void notify(TPSReportStatus status, final StatusNotifier notifier) {
        status.accept(new VoidVisitor() {
            
            @Override
            public void visit(Denied x) {
                notifier.notifyDenied();
            }
            
            @Override
            public void visit(Approved x) {
                notifier.notifyApproved();                
            }
            
            @Override
            public void visit(Pending x) {
                notifier.notifyPending();
            }
        });
    }
    // END SNIPPET: notify
    
    // START SNIPPET: notifyDenied
    public void notifyDenied(TPSReportStatus status, final StatusNotifier notifier) {
        status.accept(new VoidVisitorWithDefault() {
            
            @Override
            public void visit(Denied x) {
                notifier.notifyDenied();
            }

            @Override
            protected void doDefault(TPSReportStatus x) {
                // nothing to do if it wasn't denied                
            }
        });
    }
    // END SNIPPET: notifyDenied
    
    /**
     * Same thing using instancof
     */
    // START SNIPPET: isApprovedV2
    public boolean isApprovedV2(TPSReportStatus status) {
        return status instanceof TPSReportStatus.Approved;
     }    
    // END SNIPPET: isApprovedV2
    
    public static interface StatusNotifier {

        void notifyApproved();

        void notifyPending();

        void notifyDenied();
        
    }
}

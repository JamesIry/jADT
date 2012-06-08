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

import pogofish.jadt.samples.whathow.TPSReportStatus.*;

/**
 * Example usage of TPSReport for the how page
 */
public class TPSReportUsage {
    /**
     * Get approval status of TPSReport
     */
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
    
    /**
     * Same thing using instancof
     */
    public boolean isApprovedV2(TPSReportStatus status) {
        return status instanceof TPSReportStatus.Approved;
     }    
}

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
/*
 * This jADT file is an example AST for a toy language.
 * It is marked up with START SNIPPET and END SNIPPET boundaries 
 * to support /jADT/src/site/apt/*.apt
 */
package com.pogofish.jadt.samples.whathow.data;

/*
This file was generated based on /Users/jiry/workspace/JADT/jADT-samples/src/main/jadt/WhatHowSamples.jadt using jADT version 0.3.0-SNAPSHOT http://jamesiry.github.com/jADT/ . Please do not modify directly.

The source was parsed as: 

TPSReportStatus =
    Pending
  | Approved(final Manager approver)
  | Denied(final Manager rejector)
*/
public abstract class TPSReportStatus {

   private TPSReportStatus() {
   }

   private static final TPSReportStatus _Pending = new Pending();
   public static final  TPSReportStatus _Pending() { return _Pending; }
   public static final  TPSReportStatus _Approved(Manager approver) { return new Approved(approver); }
   public static final  TPSReportStatus _Denied(Manager rejector) { return new Denied(rejector); }

   public static interface MatchBlock<ResultType> {
      ResultType _case(Pending x);
      ResultType _case(Approved x);
      ResultType _case(Denied x);
   }

   public static abstract class MatchBlockWithDefault<ResultType> implements MatchBlock<ResultType> {
      @Override
      public ResultType _case(Pending x) { return _default(x); }

      @Override
      public ResultType _case(Approved x) { return _default(x); }

      @Override
      public ResultType _case(Denied x) { return _default(x); }

      protected abstract ResultType _default(TPSReportStatus x);
   }

   public static interface SwitchBlock {
      void _case(Pending x);
      void _case(Approved x);
      void _case(Denied x);
   }

   public static abstract class SwitchBlockWithDefault implements SwitchBlock {
      @Override
      public void _case(Pending x) { _default(x); }

      @Override
      public void _case(Approved x) { _default(x); }

      @Override
      public void _case(Denied x) { _default(x); }

      protected abstract void _default(TPSReportStatus x);
   }

   public static final class Pending extends TPSReportStatus {

      public Pending() {
      }

      @Override
      public <ResultType> ResultType match(MatchBlock<ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock switchBlock) { switchBlock._case(this); }

      @Override
      public int hashCode() {
          return 0;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Pending";
      }

   }

   public static final class Approved extends TPSReportStatus {
      public final Manager approver;

      public Approved(Manager approver) {
         this.approver = approver;
      }

      @Override
      public <ResultType> ResultType match(MatchBlock<ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock switchBlock) { switchBlock._case(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((approver == null) ? 0 : approver.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Approved other = (Approved)obj;
         if (approver == null) {
            if (other.approver != null) return false;
         } else if (!approver.equals(other.approver)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Approved(approver = " + approver + ")";
      }

   }

   public static final class Denied extends TPSReportStatus {
      public final Manager rejector;

      public Denied(Manager rejector) {
         this.rejector = rejector;
      }

      @Override
      public <ResultType> ResultType match(MatchBlock<ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock switchBlock) { switchBlock._case(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((rejector == null) ? 0 : rejector.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Denied other = (Denied)obj;
         if (rejector == null) {
            if (other.rejector != null) return false;
         } else if (!rejector.equals(other.rejector)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Denied(rejector = " + rejector + ")";
      }

   }

   public abstract <ResultType> ResultType match(MatchBlock<ResultType> matchBlock);

   public abstract void _switch(SwitchBlock switchBlock);

}
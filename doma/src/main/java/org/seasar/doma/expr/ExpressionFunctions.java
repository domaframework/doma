/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.expr;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * 式の中で利用可能な関数群です。
 * 
 * @author taedium
 * 
 */
public interface ExpressionFunctions {

    /**
     * 前方一致検索を行うことを示します。
     * 
     * @param text
     *            文字列
     * @return 前方一致検索のための文字列
     */
    String startWith(String text);

    /**
     * エスケープ文字を指定して前方一致検索を行うことを示します。
     * 
     * @param text
     *            文字列
     * @param escape
     *            エスケープ文字
     * @return 前方一致検索のための文字列
     */
    String startWith(String text, char escape);

    /**
     * 後方一致検索を行うことを示します。
     * 
     * @param text
     *            文字列
     * @return 後方一致検索のための文字列
     */
    String endWith(String text);

    /**
     * エスケープ文字を指定して後方一致検索を行うことを示します。
     * 
     * @param text
     *            文字列
     * @param escape
     *            エスケープ文字
     * @return 後方一致検索のための文字列
     */
    String endWith(String text, char escape);

    /**
     * 前方後方一致検索を行うことを示します。
     * 
     * @param text
     *            文字列
     * @return 前方後方一致検索のための文字列
     */
    String contain(String text);

    /**
     * エスケープ文字を指定して前方後方一致検索を行うことを示します。
     * 
     * @param text
     *            文字列
     * @param escape
     *            エスケープ文字
     * @return 前方後方一致検索のための文字列
     */
    String contain(String text, char escape);

    /**
     * 日付の時刻部分を最小化します。
     * 
     * @param date
     *            日付
     * @return 時刻部分が最小化された日付
     */
    Date minimizeTimePart(Date date);

    /**
     * タイムスタンプの時刻部分を最小化します。
     * 
     * @param timestamp
     *            タイムスタンプ
     * @return 時刻部分が最小化されたタイムスタンプ
     */
    Timestamp minimizeTimePart(Timestamp timestamp);

    /**
     * 日付の時刻部分を最大化します。
     * 
     * @param date
     *            日付
     * @return 時刻部分が最大化された日付
     */
    Date maximizeTimePart(Date date);

    /**
     * タイムスタンプの時刻部分を最大化します。
     * 
     * @param timestamp
     *            タイムスタンプ
     * @return 時刻部分が最大化されたタイムスタンプ
     */
    Timestamp maximizeTimePart(Timestamp timestamp);
}

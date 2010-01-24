/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
     * Like演算子用のエスケープを行います。
     * 
     * @param text
     *            エスケープ対象の文字列
     * @return エスケープされた文字列
     */
    String escape(String text);

    /**
     * Like演算子用のエスケープを行います。
     * 
     * @param text
     *            エスケープ対象の文字列
     * @param escapeChar
     *            エスケープ文字
     * @return エスケープされた文字列
     */
    String escape(String text, char escapeChar);

    /**
     * 前方一致検索を行うことを示します。
     * 
     * @param prefix
     *            前に置かれる文字列
     * @return 前方一致検索のための文字列
     */
    String prefix(String prefix);

    /**
     * エスケープ文字を指定して前方一致検索を行うことを示します。
     * 
     * @param prefix
     *            前に置かれる文字列
     * @param escapeChar
     *            エスケープ文字
     * @return 前方一致検索のための文字列
     */
    String prefix(String prefix, char escapeChar);

    /**
     * 後方一致検索を行うことを示します。
     * 
     * @param suffix
     *            後に置かれる文字列
     * @return 後方一致検索のための文字列
     */
    String suffix(String suffix);

    /**
     * エスケープ文字を指定して後方一致検索を行うことを示します。
     * 
     * @param suffix
     *            後に置かれる文字列
     * @param escapeChar
     *            エスケープ文字
     * @return 後方一致検索のための文字列
     */
    String suffix(String suffix, char escapeChar);

    /**
     * 中間一致検索を行うことを示します。
     * 
     * @param inside
     *            含まれる文字列
     * @return 中間一致検索のための文字列
     */
    String contain(String inside);

    /**
     * エスケープ文字を指定して中間一致検索を行うことを示します。
     * 
     * @param inside
     *            含まれる文字列
     * @param escapeChar
     *            エスケープ文字
     * @return 中間一致検索のための文字列
     */
    String contain(String inside, char escapeChar);

    /**
     * 日付の時刻部分を切り捨てます。
     * 
     * @param date
     *            日付
     * @return 時刻部分が切り捨てられた日付
     */
    Date roundDownTimePart(Date date);

    /**
     * タイムスタンプの時刻部分を切り捨てます。
     * 
     * @param timestamp
     *            タイムスタンプ
     * @return 時刻部分が切り捨てられたタイムスタンプ
     */
    Timestamp roundDownTimePart(Timestamp timestamp);

    /**
     * 日付の時刻部分を切り上げます。
     * 
     * @param date
     *            日付
     * @return 時刻部分が切り上げられた日付
     */
    Date roundUpTimePart(Date date);

    /**
     * タイムスタンプの時刻部分を切り上げます。
     * 
     * @param timestamp
     *            タイムスタンプ
     * @return 時刻部分が切り上げられたタイムスタンプ
     */
    Timestamp roundUpTimePart(Timestamp timestamp);
}

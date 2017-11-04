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
import java.time.LocalDate;
import java.time.LocalDateTime;

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
     *            エスケープ対象の文字シーケンス
     * @return エスケープされた文字列
     */
    String escape(CharSequence text);

    /**
     * Like演算子用のエスケープを行います。
     * 
     * @param text
     *            エスケープ対象の文字シーケンス
     * @param escapeChar
     *            エスケープ文字
     * @return エスケープされた文字列
     */
    String escape(CharSequence text, char escapeChar);

    /**
     * 前方一致検索を行うことを示します。
     * 
     * @param prefix
     *            前に置かれる文字シーケンス
     * @return 前方一致検索のための文字列
     */
    String prefix(CharSequence prefix);

    /**
     * エスケープ文字を指定して前方一致検索を行うことを示します。
     * 
     * @param prefix
     *            前に置かれる文字シーケンス
     * @param escapeChar
     *            エスケープ文字
     * @return 前方一致検索のための文字列
     */
    String prefix(CharSequence prefix, char escapeChar);

    /**
     * 後方一致検索を行うことを示します。
     * 
     * @param suffix
     *            後に置かれる文字シーケンス
     * @return 後方一致検索のための文字列
     */
    String suffix(CharSequence suffix);

    /**
     * エスケープ文字を指定して後方一致検索を行うことを示します。
     * 
     * @param suffix
     *            後に置かれる文字シーケンス
     * @param escapeChar
     *            エスケープ文字
     * @return 後方一致検索のための文字列
     */
    String suffix(CharSequence suffix, char escapeChar);

    /**
     * 中間一致検索を行うことを示します。
     * 
     * @param infix
     *            含まれる文字シーケンス
     * @return 中間一致検索のための文字列
     * @since 1.33.0
     */
    String infix(CharSequence infix);

    /**
     * エスケープ文字を指定して中間一致検索を行うことを示します。
     * 
     * @param infix
     *            含まれる文字シーケンス
     * @param escapeChar
     *            エスケープ文字
     * @return 中間一致検索のための文字列
     * @since 1.33.0
     */
    String infix(CharSequence infix, char escapeChar);

    /**
     * 日付の時刻部分を切り捨てます。
     * 
     * @param date
     *            日付
     * @return 時刻部分が切り捨てられた日付
     * @since 1.33.0
     */
    java.util.Date roundDownTimePart(java.util.Date date);

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
     * LocalDateTime の時刻部分を切り捨てます。
     * 
     * @param localDateTime
     *            LocalDateTime
     * @return 時刻部分が切り捨てられたタイムスタンプ
     */
    LocalDateTime roundDownTimePart(LocalDateTime localDateTime);

    /**
     * 日付の時刻部分を切り上げます。
     * 
     * @param date
     *            日付
     * @return 時刻部分が切り上げられた日付
     * @since 1.33.0
     */
    java.util.Date roundUpTimePart(java.util.Date date);

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

    /**
     * 翌日の日付を返します。
     * 
     * @param localDate
     *            LocalDate
     * @return 翌日の日付
     */
    LocalDate roundUpTimePart(LocalDate localDate);

    /**
     * LocalDateTime の時刻部分を切り上げます。
     * 
     * @param localDateTime
     *            LocalDateTime
     * @return 時刻部分が切り上げられたタイムスタンプ
     */
    LocalDateTime roundUpTimePart(LocalDateTime localDateTime);

    /**
     * 文字シーケンスが {@code null}、もしくは文字シーケンスの長さが {@code 0} の場合 {@code true} を返します。
     * 
     * @param charSequence
     *            文字シーケンス
     * @return 文字シーケンスが {@code null}、もしくは文字シーケンスの長さが {@code 0} の場合 {@code true}
     * @since 1.3.0
     */
    boolean isEmpty(CharSequence charSequence);

    /**
     * 文字シーケンスが {@code null} でない、かつ文字シーケンスの長さが {@code 0} でない場合 {@code true}
     * を返します。
     * 
     * @param charSequence
     *            文字シーケンス
     * @return 文字シーケンスが {@code null} でない、かつ文字シーケンスの長さが {@code 0} でない場合
     *         {@code true}
     * @since 1.3.0
     */
    boolean isNotEmpty(CharSequence charSequence);

    /**
     * 文字シーケンスが {@code null}、もしくは文字シーケンスの長さが {@code 0}、もしくは文字シーケンスが空白だけから形成される場合
     * {@code true} を返します。
     * 
     * @param charSequence
     *            文字シーケンス
     * @return 文字シーケンスが{@code null}、もしくは文字シーケンスの長さが {@code 0}
     *         、もしくは文字シーケンスが空白だけから形成される場合 {@code true}
     * @since 1.3.0
     */
    boolean isBlank(CharSequence charSequence);

    /**
     * 文字シーケンスが {@code null} でない、かつ文字シーケンスの長さが {@code 0}
     * でない、かつ文字シーケンスが空白だけで形成されない場合 {@code true} を返します。
     * 
     * @param charSequence
     *            文字シーケンス
     * @return 文字シーケンスが {@code null} でない、かつ文字シーケンスの長さが {@code 0}
     *         でない、かつ文字シーケンスが空白だけで形成されない場合 {@code true}
     * @since 1.3.0
     */
    boolean isNotBlank(CharSequence charSequence);
}

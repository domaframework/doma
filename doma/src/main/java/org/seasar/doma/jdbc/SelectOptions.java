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
package org.seasar.doma.jdbc;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;

/**
 * 検索系SQLを実行する際のオプションです。
 * <p>
 * {@link #get()}でインスタンスを取得し、メソッド呼び出しをチェインさせることができます。
 * 
 * <h5>例</h5>
 * 
 * <pre>
 * SelectOptions options = SelectOptions.get().offset(10).limit(50).forUpdate();
 * </pre>
 * 
 * @author taedium
 * 
 */
public class SelectOptions {

    /** ページングのオフセット */
    protected int offset = -1;

    /** ページングのリミット */
    protected int limit = -1;

    /** 悲観的排他制御の種別 */
    protected SelectForUpdateType forUpdateType;

    /** 悲観的排他制御の待機時間（秒） */
    protected int waitSeconds;

    /** 悲観的排他制御のロック対象のエイリアスの配列 */
    protected String[] aliases = new String[] {};

    /**
     * インスタンスを構築します。
     */
    protected SelectOptions() {
    }

    /**
     * インスタンスを取得するためのファクトリメソッドです。
     * 
     * @return 新しい {@link SelectOptions}
     */
    public static SelectOptions get() {
        return new SelectOptions();
    }

    /**
     * 悲観的排他制御用のSQLへ変換することを示します。
     * 
     * @return このインスタンス
     */
    public SelectOptions forUpdate() {
        forUpdateType = SelectForUpdateType.NORMAL;
        return this;
    }

    /**
     * ロック対象のテーブルやカラムのエイリアスを指定し、悲観的排他制御用のSQLへ変換することを示します。
     * 
     * @param aliases
     *            テーブルやカラムのエイリアス
     * @return このインスタンス
     */
    public SelectOptions forUpdate(String... aliases) {
        if (aliases == null) {
            new DomaNullPointerException("aliases");
        }
        forUpdateType = SelectForUpdateType.NORMAL;
        this.aliases = aliases;
        return this;
    }

    /**
     * ロックの取得を待機しない悲観的排他制御用のSQLへ変換することを示します。
     * 
     * @return このインスタンス
     */
    public SelectOptions forUpdateNowait() {
        forUpdateType = SelectForUpdateType.NOWAIT;
        return this;
    }

    /**
     * ロック対象のテーブルやカラムのエイリアスを指定し、ロックの取得を待機しない悲観的排他制御用のSQLへ変換することを示します。
     * 
     * @param aliases
     *            テーブルやカラムのエイリアス
     * @return このインスタンス
     */
    public SelectOptions forUpdateNowait(String... aliases) {
        if (aliases == null) {
            new DomaNullPointerException("aliases");
        }
        forUpdateType = SelectForUpdateType.NOWAIT;
        this.aliases = aliases;
        return this;
    }

    /**
     * ロックの取得まで指定された時間待機する悲観的排他制御用のSQLへ変換することを示します。
     * 
     * @param waitSeconds
     *            待機時間（秒）
     * @return このインスタンス
     */
    public SelectOptions forUpdateWait(int waitSeconds) {
        if (waitSeconds < 0) {
            new DomaIllegalArgumentException("waitSeconds", "waitSeconds < 0");
        }
        forUpdateType = SelectForUpdateType.WAIT;
        this.waitSeconds = waitSeconds;
        return this;
    }

    /**
     * ロック対象のテーブルやカラムのエイリアスを指定し、ロックの取得まで指定された時間待機する悲観的排他制御用のSQLへ変換することを示します。
     * 
     * @param waitSeconds
     *            待機時間（秒）
     * @param aliases
     *            テーブルやカラムのエイリアス
     * @return このインスタンス
     */
    public SelectOptions forUpdateWait(int waitSeconds, String... aliases) {
        if (waitSeconds < 0) {
            new DomaIllegalArgumentException("waitSeconds", "waitSeconds < 0");
        }
        if (aliases == null) {
            new DomaNullPointerException("aliases");
        }
        forUpdateType = SelectForUpdateType.WAIT;
        this.waitSeconds = waitSeconds;
        this.aliases = aliases;
        return this;
    }

    /**
     * オフセットを指定してページング用のSQLへ変換することを示します。
     * 
     * @param offset
     *            オフセット
     * @return このインスタンス
     */
    public SelectOptions offset(int offset) {
        if (offset < 0) {
            new DomaIllegalArgumentException("offset", "offset < 0");
        }
        this.offset = offset;
        return this;
    }

    /**
     * リミットを指定してページング用のSQLへ変換することを示します。
     * 
     * @param limit
     *            リミット
     * @return このインスタンス
     */
    public SelectOptions limit(int limit) {
        if (limit < 0) {
            new DomaIllegalArgumentException("limit", "limit < 0");
        }
        this.limit = limit;
        return this;
    }

    /**
     * 悲観的排他制御の種別を返します。
     * 
     * @return 悲観的排他制御の種別
     */
    public SelectForUpdateType getForUpdateType() {
        return forUpdateType;
    }

    /**
     * 悲観的排他制御のロック取得の待機時間を返します。
     * 
     * @return 待機時間（秒）
     */
    public int getWaitSeconds() {
        return waitSeconds;
    }

    /**
     * ロック対象のテーブルもしくはカラムのエイリアスの配列を返します。
     * 
     * @return テーブルもしくはカラムのエイリアスの配列
     */
    public String[] getAliases() {
        return aliases;
    }

    /**
     * ページングのオフセットを返します。
     * 
     * @return オフセット
     */
    public int getOffset() {
        return offset;
    }

    /**
     * ページングのリミットを返します。
     * 
     * @return リミット
     */
    public int getLimit() {
        return limit;
    }

}

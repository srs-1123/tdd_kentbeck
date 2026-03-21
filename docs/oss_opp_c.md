# C言語におけるオブジェクト指向プラクティス集

本リポジトリに含まれるOSSライブラリから抽出した、C言語でオブジェクト指向設計を実現するための実装パターン集。
設計の優れている順（多態性・カプセル化・再利用性の観点）にセクションを配置している。

---

## 目次

1. [vtable（仮想関数テーブル）パターン — libnl 3.2.27](#1-vtable仮想関数テーブルパターン--libnl-3227)
   - [1.1 共通オブジェクトヘッダによる擬似継承](#11-共通オブジェクトヘッダによる擬似継承)
   - [1.2 仮想関数テーブル（nl_object_ops）](#12-仮想関数テーブルnl_object_ops)
   - [1.3 オブジェクトの生成（コンストラクタ）](#13-オブジェクトの生成コンストラクタ)
   - [1.4 オブジェクトの複製（クローン）](#14-オブジェクトの複製クローン)
   - [1.5 オブジェクトの破棄（デストラクタ）](#15-オブジェクトの破棄デストラクタ)
   - [1.6 参照カウントによるライフサイクル管理](#16-参照カウントによるライフサイクル管理)
   - [1.7 キャッシュ操作テーブル（nl_cache_ops）](#17-キャッシュ操作テーブルnl_cache_ops)
   - [1.8 ソケットオブジェクトの管理](#18-ソケットオブジェクトの管理)
2. [container_of 継承 + 参照カウントパターン — Jansson 2.14](#2-container_of-継承--参照カウントパターン--jansson-214)
   - [2.1 基底型（json_t）の定義](#21-基底型json_tの定義)
   - [2.2 派生型による擬似継承（構造体埋め込み）](#22-派生型による擬似継承構造体埋め込み)
   - [2.3 container_of によるダウンキャスト](#23-container_of-によるダウンキャスト)
   - [2.4 型チェックマクロ（RTTI相当）](#24-型チェックマクロrtti相当)
   - [2.5 アトミック参照カウント](#25-アトミック参照カウント)
   - [2.6 型ごとのファクトリ関数](#26-型ごとのファクトリ関数)
   - [2.7 シングルトンパターン](#27-シングルトンパターン)
   - [2.8 統一デストラクタ（型ディスパッチ）](#28-統一デストラクタ型ディスパッチ)
3. [BIO_METHOD vtable + ファクトリパターン — OpenSSL 3.0.16](#3-bio_method-vtable--ファクトリパターン--openssl-3016)
   - [3.1 不透明型の定義（typedef による情報隠蔽）](#31-不透明型の定義typedef-による情報隠蔽)
   - [3.2 BIO_METHOD 仮想関数テーブル](#32-bio_method-仮想関数テーブル)
   - [3.3 BIO_METHOD のファクトリと vtable セッター](#33-bio_method-のファクトリと-vtable-セッター)
   - [3.4 BIO インスタンス構造体（内部実装）](#34-bio-インスタンス構造体内部実装)
   - [3.5 BIO のコンストラクタ（vtable による初期化分岐）](#35-bio-のコンストラクタvtable-による初期化分岐)
   - [3.6 BIO のデストラクタ（参照カウント付き）](#36-bio-のデストラクタ参照カウント付き)
   - [3.7 EVP コンテキストの生成・破棄](#37-evp-コンテキストの生成破棄)
   - [3.8 SSL/SSL_CTX の階層的オブジェクト生成](#38-sslssl_ctx-の階層的オブジェクト生成)
4. [不透明ハンドル + マルチインスタンスパターン — libcurl 8.10.1](#4-不透明ハンドル--マルチインスタンスパターン--libcurl-8101)
   - [4.1 公開API vs 内部実装の二重定義](#41-公開api-vs-内部実装の二重定義)
   - [4.2 Easy API（単一セッション管理）](#42-easy-api単一セッション管理)
   - [4.3 コンストラクタの実装](#43-コンストラクタの実装)
   - [4.4 デストラクタの実装](#44-デストラクタの実装)
   - [4.5 duphandle（プロトタイプ複製パターン）](#45-duphandleプロトタイプ複製パターン)
   - [4.6 Multi API（コンテナオブジェクト）](#46-multi-apiコンテナオブジェクト)
   - [4.7 内部オブジェクト構造体](#47-内部オブジェクト構造体)
5. [設定/接続分離パターン — s2n-tls](#5-設定接続分離パターン--s2n-tls)
   - [5.1 不透明型の前方宣言](#51-不透明型の前方宣言)
   - [5.2 コンストラクタ/デストラクタ群](#52-コンストラクタデストラクタ群)
   - [5.3 コールバック登録パターン](#53-コールバック登録パターン)
6. [階層的オブジェクトモデル — FreeType 2.3.5](#6-階層的オブジェクトモデル--freetype-235)
   - [6.1 ハンドル型定義の階層](#61-ハンドル型定義の階層)
   - [6.2 汎用データアタッチメント（FT_Generic）](#62-汎用データアタッチメントft_generic)
   - [6.3 ライブラリ/フェイスの生成・破棄](#63-ライブラリフェイスの生成破棄)
   - [6.4 オブジェクト階層の構造体定義](#64-オブジェクト階層の構造体定義)
7. [プラグイン/コールバックアーキテクチャ — libwebsockets 4.3.3](#7-プラグインコールバックアーキテクチャ--libwebsockets-433)
   - [7.1 コンテキスト生成構造体](#71-コンテキスト生成構造体)
   - [7.2 プロトコルハンドラ（Strategy パターン）](#72-プロトコルハンドラstrategy-パターン)
   - [7.3 プラグインシステム](#73-プラグインシステム)
8. [ポリシーベースオブジェクト生成 — libsrtp2](#8-ポリシーベースオブジェクト生成--libsrtp2)
   - [8.1 不透明セッション型](#81-不透明セッション型)
   - [8.2 ポリシー構造体による設定注入](#82-ポリシー構造体による設定注入)
   - [8.3 ライブラリ初期化とセッション管理](#83-ライブラリ初期化とセッション管理)
9. [設計パターン横断比較表](#9-設計パターン横断比較表)

---

## 1. vtable（仮想関数テーブル）パターン — libnl 3.2.27

C言語で**最も完成度の高いOOP実装**。C++のクラス継承・仮想関数・参照カウントに相当する機構をすべてCのみで実現している。

### 1.1 共通オブジェクトヘッダによる擬似継承

すべてのNetlinkオブジェクトが共有するヘッダをマクロで定義し、構造体の先頭に配置することでC++の基底クラスを模倣する。

**ファイル:** `lib/external/libnl-3.2.27/include/netlink-private/object-api.h`

```c
/* 共通ヘッダマクロ — すべてのオブジェクトが先頭にこれを持つ */
#define NLHDR_COMMON                \
    int             ce_refcnt;      \
    struct nl_object_ops *  ce_ops; \
    struct nl_cache *       ce_cache; \
    struct nl_list_head     ce_list;  \
    int             ce_msgtype;     \
    int             ce_flags;       \
    uint32_t        ce_mask;

/* 基底オブジェクト構造体 */
struct nl_object
{
    NLHDR_COMMON
};
```

**設計ポイント:**
- `NLHDR_COMMON` マクロを各派生構造体の先頭に展開することで、任意の派生型を `struct nl_object *` にキャストしても安全にアクセスできる
- `ce_ops` が仮想関数テーブルへのポインタ（C++のvptrに相当）
- `ce_refcnt` で参照カウントを実現

### 1.2 仮想関数テーブル（nl_object_ops）

C++の仮想関数テーブル（vtable）に相当する構造体。オブジェクトの型ごとに異なる実装を登録する。

**ファイル:** `lib/external/libnl-3.2.27/include/netlink-private/object-api.h`

```c
struct nl_object_ops
{
    /** 型名（"route/addr" のような family/name 形式） */
    char *      oo_name;

    /** ヘッダを含むオブジェクトのサイズ */
    size_t      oo_size;

    /** 一意識別に必要な属性ビットマスク */
    uint32_t    oo_id_attrs;

    /**
     * コンストラクタ — 新規割り当て時に呼ばれる
     * リストの初期化などに利用
     */
    void  (*oo_constructor)(struct nl_object *);

    /**
     * デストラクタ — 解放時に呼ばれる
     * オブジェクト固有のリソースを解放する
     */
    void  (*oo_free_data)(struct nl_object *);

    /**
     * クローン — オブジェクト複製時に呼ばれる
     * 汎用コードがまず完全コピーを作るため、
     * 参照カウントが必要なメンバだけ処理すればよい
     */
    int  (*oo_clone)(struct nl_object *, struct nl_object *);

    /**
     * ダンプ関数群 — デバッグ出力用
     * 複数の詳細レベルに対応
     */
    void (*oo_dump[NL_DUMP_MAX+1])(struct nl_object *,
                                   struct nl_dump_params *);

    /**
     * 比較関数 — 同じ型の2オブジェクトを比較
     * ミスマッチした属性のビットマスクを返す
     */
    int   (*oo_compare)(struct nl_object *, struct nl_object *,
                        uint32_t, int);

    /**
     * 更新関数 — 第1引数のオブジェクトを第2引数で更新
     */
    int   (*oo_update)(struct nl_object *, struct nl_object *);

    /**
     * ハッシュキー生成 — 連想配列のキー計算用
     */
    void   (*oo_keygen)(struct nl_object *, uint32_t *, uint32_t);

    char *(*oo_attrs2str)(int, char *, size_t);

    /** ファミリ別のキー属性取得 */
    uint32_t   (*oo_id_attrs_get)(struct nl_object *);
};
```

**設計ポイント:**
- 各関数ポインタが `struct nl_object *` を第1引数に取る（C++の`this`ポインタに相当）
- `oo_dump` は配列で複数の詳細レベルに対応（C++のオーバーロードに相当する発想）
- `oo_compare` は戻り値でミスマッチ属性をビットマスクで返す（型安全な比較インターフェース）

### 1.3 オブジェクトの生成（コンストラクタ）

**ファイル:** `lib/external/libnl-3.2.27/lib/object.c`

```c
struct nl_object *nl_object_alloc(struct nl_object_ops *ops)
{
    struct nl_object *new;

    if (ops->oo_size < sizeof(*new))
        BUG();

    new = calloc(1, ops->oo_size);
    if (!new)
        return NULL;

    new->ce_refcnt = 1;
    nl_init_list_head(&new->ce_list);

    new->ce_ops = ops;
    if (ops->oo_constructor)
        ops->oo_constructor(new);

    NL_DBG(4, "Allocated new object %p\n", new);

    return new;
}
```

**設計ポイント:**
- `ops->oo_size` によって派生型のサイズ分のメモリを確保（C++の `new` に相当）
- 参照カウントを1で初期化（所有権の明確化）
- vtable に登録されたコンストラクタを呼び出し（仮想コンストラクタパターン）
- `calloc` でゼロ初期化し、未初期化メンバへのアクセスを防止

### 1.4 オブジェクトの複製（クローン）

**ファイル:** `lib/external/libnl-3.2.27/lib/object.c`

```c
struct nl_object *nl_object_clone(struct nl_object *obj)
{
    struct nl_object *new;
    struct nl_object_ops *ops;
    int doff = offsetof(struct nl_derived_object, data);
    int size;

    if (!obj)
        return NULL;

    ops = obj_ops(obj);
    new = nl_object_alloc(ops);
    if (!new)
        return NULL;

    size = ops->oo_size - doff;
    if (size < 0)
        BUG();

    new->ce_ops = obj->ce_ops;
    new->ce_msgtype = obj->ce_msgtype;
    new->ce_mask = obj->ce_mask;

    if (size)
        memcpy((void *)new + doff, (void *)obj + doff, size);

    if (ops->oo_clone) {
        if (ops->oo_clone(new, obj) < 0) {
            nl_object_free(new);
            return NULL;
        }
    } else if (size && ops->oo_free_data)
        BUG();

    return new;
}
```

**設計ポイント:**
- `offsetof` で共通ヘッダ部分をスキップし、派生データのみコピー
- 浅いコピー（`memcpy`）後に `oo_clone` で深いコピーを実行（二段階クローン）
- `oo_free_data` があるのに `oo_clone` がない場合はバグとして検出

### 1.5 オブジェクトの破棄（デストラクタ）

**ファイル:** `lib/external/libnl-3.2.27/lib/object.c`

```c
void nl_object_free(struct nl_object *obj)
{
    struct nl_object_ops *ops;

    if (!obj)
        return;

    ops = obj_ops(obj);

    if (obj->ce_refcnt > 0)
        NL_DBG(1, "Warning: Freeing object in use...\n");

    if (obj->ce_cache)
        nl_cache_remove(obj);

    if (ops->oo_free_data)
        ops->oo_free_data(obj);

    NL_DBG(4, "Freed object %p\n", obj);

    free(obj);
}
```

**設計ポイント:**
- `NULL` チェックによる安全な呼び出し（`delete nullptr` と同等）
- キャッシュからの自動除去（所有権の自動解放）
- vtable のデストラクタで型固有のリソースを解放してから `free`

### 1.6 参照カウントによるライフサイクル管理

**ファイル:** `lib/external/libnl-3.2.27/lib/object.c`

```c
void nl_object_get(struct nl_object *obj)
{
    obj->ce_refcnt++;
    NL_DBG(4, "New reference to object %p, total %d\n",
           obj, obj->ce_refcnt);
}

void nl_object_put(struct nl_object *obj)
{
    if (!obj)
        return;

    obj->ce_refcnt--;
    NL_DBG(4, "Returned object reference %p, %d remaining\n",
           obj, obj->ce_refcnt);

    if (obj->ce_refcnt < 0)
        BUG();

    if (obj->ce_refcnt <= 0)
        nl_object_free(obj);
}
```

**設計ポイント:**
- `get`/`put` ペアで所有権を管理（C++の `shared_ptr` に相当）
- 参照カウントが負になった場合のバグ検出
- カウントが0になった時点で自動的にデストラクタを呼び出し

### 1.7 キャッシュ操作テーブル（nl_cache_ops）

オブジェクトのvtableに加え、キャッシュレベルでもvtableパターンを適用。

**ファイル:** `lib/external/libnl-3.2.27/include/netlink-private/cache-api.h`

```c
struct nl_cache_ops
{
    /** キャッシュ型名（一意） */
    char  *             co_name;

    /** ファミリ固有Netlinkヘッダサイズ */
    int                 co_hdrsize;

    /** Netlinkプロトコル */
    int                 co_protocol;

    /** ハッシュテーブルサイズ */
    int                 co_hash_size;

    unsigned int        co_flags;
    unsigned int        co_refcnt;

    struct nl_af_group *co_groups;

    /** キャッシュ更新要求 — カーネルへダンプリクエストを送信 */
    int   (*co_request_update)(struct nl_cache *, struct nl_sock *);

    /** メッセージパーサー — 受信メッセージをオブジェクトに変換 */
    int   (*co_msg_parser)(struct nl_cache_ops *, struct sockaddr_nl *,
                           struct nlmsghdr *, struct nl_parser_param *);

    /** イベントフィルター — 通知を取り込むか判定 */
    int   (*co_event_filter)(struct nl_cache *, struct nl_object *obj);

    /** イベント取り込み — 通知からオブジェクトをキャッシュに追加 */
    int   (*co_include_event)(struct nl_cache *cache, struct nl_object *obj,
                              change_func_t change_cb, void *data);

    /* 将来の拡張用の予約スロット */
    void (*reserved_1)(void);
    void (*reserved_2)(void);
    void (*reserved_3)(void);
    void (*reserved_4)(void);
    void (*reserved_5)(void);
    void (*reserved_6)(void);
    void (*reserved_7)(void);
    void (*reserved_8)(void);

    /** 管理するオブジェクトの操作テーブル */
    struct nl_object_ops *  co_obj_ops;

    /** 内部使用：次のキャッシュ操作へのリンク */
    struct nl_cache_ops *co_next;

    struct nl_cache *co_major_cache;
    struct genl_ops *   co_genl;

    /** メッセージ型定義（可変長配列） */
    struct nl_msgtype   co_msgtypes[];
};
```

**設計ポイント:**
- `reserved_*` スロットによりABI互換性を維持しつつ将来の拡張に対応
- `co_obj_ops` で管理対象オブジェクトのvtableを参照（コンポジション）
- 可変長配列メンバ `co_msgtypes[]` による柔軟なメッセージ型定義

### 1.8 ソケットオブジェクトの管理

**ファイル:** `lib/external/libnl-3.2.27/lib/socket.c`

```c
/* プライベートコンストラクタ（内部用） */
static struct nl_sock *__alloc_socket(struct nl_cb *cb)
{
    struct nl_sock *sk;

    sk = calloc(1, sizeof(*sk));
    if (!sk)
        return NULL;

    sk->s_fd = -1;
    sk->s_cb = nl_cb_get(cb);  /* コールバックの参照カウントを増加 */
    sk->s_local.nl_family = AF_NETLINK;
    sk->s_peer.nl_family = AF_NETLINK;
    sk->s_seq_expect = sk->s_seq_next = time(NULL);
    sk->s_flags = NL_OWN_PORT;

    return sk;
}

/* パブリックコンストラクタ */
struct nl_sock *nl_socket_alloc(void)
{
    struct nl_cb *cb;
    struct nl_sock *sk;

    cb = nl_cb_alloc(default_cb);
    if (!cb)
        return NULL;

    /* 成功時にcbの参照カウントを増加 */
    sk = __alloc_socket(cb);

    nl_cb_put(cb);  /* ローカル参照を解放 */

    return sk;
}

/* デストラクタ */
void nl_socket_free(struct nl_sock *sk)
{
    if (!sk)
        return;

    if (sk->s_fd >= 0)
        close(sk->s_fd);

    if (!(sk->s_flags & NL_OWN_PORT))
        release_local_port(sk->s_local.nl_pid);

    nl_cb_put(sk->s_cb);  /* コールバックの参照カウントを減少 */
    free(sk);
}
```

**ファイル:** `lib/external/libnl-3.2.27/include/netlink-private/types.h`（内部構造体）

```c
struct nl_sock
{
    struct sockaddr_nl  s_local;
    struct sockaddr_nl  s_peer;
    int                 s_fd;
    int                 s_proto;
    unsigned int        s_seq_next;
    unsigned int        s_seq_expect;
    int                 s_flags;
    struct nl_cb *      s_cb;
    size_t              s_bufsize;
};
```

**設計ポイント:**
- `__alloc_socket` をプライベート関数にし、パブリックAPIから分離（C++の private コンストラクタに相当）
- コールバックオブジェクトを `nl_cb_get`/`nl_cb_put` で参照カウント管理
- デストラクタでファイルディスクリプタの確実なクローズとリソース解放

---

## 2. container_of 継承 + 参照カウントパターン — Jansson 2.14

Linuxカーネルでも使われる `container_of` マクロを活用し、基底型の埋め込みによる擬似継承を実現。
参照カウントにはアトミック操作を採用し、スレッドセーフ性も確保している。

### 2.1 基底型（json_t）の定義

**ファイル:** `lib/external/jansson-2.14/src/jansson.h`

```c
typedef enum {
    JSON_OBJECT,
    JSON_ARRAY,
    JSON_STRING,
    JSON_INTEGER,
    JSON_REAL,
    JSON_TRUE,
    JSON_FALSE,
    JSON_NULL
} json_type;

typedef struct json_t {
    json_type type;
    volatile size_t refcount;
} json_t;
```

**設計ポイント:**
- `type` フィールドが型判別子（C++の `typeid` に相当）
- `volatile` で最適化による参照カウントの不整合を防止
- 最小限のメンバでメモリ効率を維持

### 2.2 派生型による擬似継承（構造体埋め込み）

**ファイル:** `lib/external/jansson-2.14/src/jansson_private.h`

```c
typedef struct {
    json_t json;            /* 基底型を先頭に配置 */
    hashtable_t hashtable;  /* オブジェクト固有データ */
} json_object_t;

typedef struct {
    json_t json;
    size_t size;
    size_t entries;
    json_t **table;
} json_array_t;

typedef struct {
    json_t json;
    char *value;
    size_t length;
} json_string_t;

typedef struct {
    json_t json;
    double value;
} json_real_t;

typedef struct {
    json_t json;
    json_int_t value;
} json_integer_t;
```

**設計ポイント:**
- `json_t` を各派生構造体の**先頭メンバ**に配置（C言語の構造体レイアウト保証を利用）
- これにより `json_object_t *` を `json_t *` にキャストしても安全
- 各派生型はヘッダファイルに非公開（カプセル化の実現）

### 2.3 container_of によるダウンキャスト

**ファイル:** `lib/external/jansson-2.14/src/jansson_private.h`

```c
#define container_of(ptr_, type_, member_)  \
    ((type_ *)((char *)ptr_ - offsetof(type_, member_)))

#define json_to_object(json_)  container_of(json_, json_object_t, json)
#define json_to_array(json_)   container_of(json_, json_array_t, json)
#define json_to_string(json_)  container_of(json_, json_string_t, json)
#define json_to_real(json_)    container_of(json_, json_real_t, json)
#define json_to_integer(json_) container_of(json_, json_integer_t, json)
```

**設計ポイント:**
- `container_of` はLinuxカーネルで広く使われるイディオム
- `json_t *` から派生型のポインタを安全に逆算
- 基底型が先頭メンバなので `offsetof` は常に0（実質単純キャスト）だが、汎用性のために `container_of` を使用

### 2.4 型チェックマクロ（RTTI相当）

**ファイル:** `lib/external/jansson-2.14/src/jansson.h`

```c
#define json_typeof(json)     ((json)->type)
#define json_is_object(json)  ((json) && json_typeof(json) == JSON_OBJECT)
#define json_is_array(json)   ((json) && json_typeof(json) == JSON_ARRAY)
#define json_is_string(json)  ((json) && json_typeof(json) == JSON_STRING)
#define json_is_integer(json) ((json) && json_typeof(json) == JSON_INTEGER)
#define json_is_real(json)    ((json) && json_typeof(json) == JSON_REAL)
#define json_is_number(json)  (json_is_integer(json) || json_is_real(json))
#define json_is_true(json)    ((json) && json_typeof(json) == JSON_TRUE)
#define json_is_false(json)   ((json) && json_typeof(json) == JSON_FALSE)
#define json_boolean_value    json_is_true
#define json_is_boolean(json) (json_is_true(json) || json_is_false(json))
#define json_is_null(json)    ((json) && json_typeof(json) == JSON_NULL)
```

**設計ポイント:**
- NULLチェックを型チェックに組み込み（安全なAPI設計）
- `json_is_number` のような複合型チェックも提供
- マクロなのでインライン展開され、関数呼び出しオーバーヘッドなし

### 2.5 アトミック参照カウント

**ファイル:** `lib/external/jansson-2.14/src/jansson.h`

```c
/* コンパイラのアトミック機能に応じた最適な実装を選択 */
#if JSON_HAVE_ATOMIC_BUILTINS
#define JSON_INTERNAL_INCREF(json)  \
    __atomic_add_fetch(&json->refcount, 1, __ATOMIC_ACQUIRE)
#define JSON_INTERNAL_DECREF(json)  \
    __atomic_sub_fetch(&json->refcount, 1, __ATOMIC_RELEASE)
#elif JSON_HAVE_SYNC_BUILTINS
#define JSON_INTERNAL_INCREF(json) __sync_add_and_fetch(&json->refcount, 1)
#define JSON_INTERNAL_DECREF(json) __sync_sub_and_fetch(&json->refcount, 1)
#else
#define JSON_INTERNAL_INCREF(json) (++json->refcount)
#define JSON_INTERNAL_DECREF(json) (--json->refcount)
#endif

/* 公開API */
static JSON_INLINE json_t *json_incref(json_t *json) {
    if (json && json->refcount != (size_t)-1)
        JSON_INTERNAL_INCREF(json);
    return json;
}

void json_delete(json_t *json);

static JSON_INLINE void json_decref(json_t *json) {
    if (json && json->refcount != (size_t)-1 && JSON_INTERNAL_DECREF(json) == 0)
        json_delete(json);
}
```

**設計ポイント:**
- 3段階のフォールバック（C11アトミック → GCC sync → 非アトミック）
- `refcount == (size_t)-1` はシングルトンオブジェクト（true, false, null）を示す特別な値
- `json_incref` は自身を返すため、メソッドチェーン的な使い方が可能

### 2.6 型ごとのファクトリ関数

**ファイル:** `lib/external/jansson-2.14/src/value.c`

```c
/* 共通の初期化ヘルパー */
static JSON_INLINE void json_init(json_t *json, json_type type) {
    json->type = type;
    json->refcount = 1;
}

/* オブジェクト型の生成 */
json_t *json_object(void) {
    json_object_t *object = jsonp_malloc(sizeof(json_object_t));
    if (!object)
        return NULL;

    if (!hashtable_seed) {
        json_object_seed(0);
    }

    json_init(&object->json, JSON_OBJECT);

    if (hashtable_init(&object->hashtable)) {
        jsonp_free(object);
        return NULL;
    }

    return &object->json;  /* 基底型のポインタを返す（アップキャスト） */
}

/* 配列型の生成 */
json_t *json_array(void) {
    json_array_t *array = jsonp_malloc(sizeof(json_array_t));
    if (!array)
        return NULL;
    json_init(&array->json, JSON_ARRAY);

    array->entries = 0;
    array->size = 8;

    array->table = jsonp_malloc(array->size * sizeof(json_t *));
    if (!array->table) {
        jsonp_free(array);
        return NULL;
    }

    return &array->json;
}

/* 文字列型の生成（内部ヘルパー） */
static json_t *string_create(const char *value, size_t len, int own) {
    char *v;
    json_string_t *string;

    if (!value)
        return NULL;

    if (own)
        v = (char *)value;
    else {
        v = jsonp_strndup(value, len);
        if (!v)
            return NULL;
    }

    string = jsonp_malloc(sizeof(json_string_t));
    if (!string) {
        jsonp_free(v);
        return NULL;
    }
    json_init(&string->json, JSON_STRING);
    string->value = v;
    string->length = len;

    return &string->json;
}

/* 整数型の生成 */
json_t *json_integer(json_int_t value) {
    json_integer_t *integer = jsonp_malloc(sizeof(json_integer_t));
    if (!integer)
        return NULL;
    json_init(&integer->json, JSON_INTEGER);

    integer->value = value;
    return &integer->json;
}
```

**設計ポイント:**
- すべてのファクトリ関数が `json_t *`（基底型ポインタ）を返す
- 内部的には派生型のメモリを確保し、先頭の `json_t` メンバのポインタを返す
- `string_create` の `own` パラメータで所有権の移譲を制御（move semantics に近い発想）

### 2.7 シングルトンパターン

**ファイル:** `lib/external/jansson-2.14/src/value.c`

```c
json_t *json_true(void) {
    static json_t the_true = {JSON_TRUE, (size_t)-1};
    return &the_true;
}

json_t *json_false(void) {
    static json_t the_false = {JSON_FALSE, (size_t)-1};
    return &the_false;
}

json_t *json_null(void) {
    static json_t the_null = {JSON_NULL, (size_t)-1};
    return &the_null;
}
```

**設計ポイント:**
- `refcount = (size_t)-1` でシングルトンであることをマーク
- `json_incref`/`json_decref` はこの値を検出してカウント操作をスキップ
- `static` ローカル変数でスレッドセーフなシングルトン（C11以降）
- メモリ割り当てなしで不変値を返す（パフォーマンス最適化）

### 2.8 統一デストラクタ（型ディスパッチ）

**ファイル:** `lib/external/jansson-2.14/src/value.c`

```c
/* 型ごとのプライベートデストラクタ */
static void json_delete_object(json_object_t *object) {
    hashtable_close(&object->hashtable);
    jsonp_free(object);
}

static void json_delete_array(json_array_t *array) {
    size_t i;
    for (i = 0; i < array->entries; i++)
        json_decref(array->table[i]);  /* 子要素の参照カウントを減少 */
    jsonp_free(array->table);
    jsonp_free(array);
}

/* 統一デストラクタ — 型に応じて適切なデストラクタにディスパッチ */
void json_delete(json_t *json) {
    if (!json)
        return;

    switch (json_typeof(json)) {
        case JSON_OBJECT:
            json_delete_object(json_to_object(json));
            break;
        case JSON_ARRAY:
            json_delete_array(json_to_array(json));
            break;
        case JSON_STRING:
            json_delete_string(json_to_string(json));
            break;
        case JSON_INTEGER:
            json_delete_integer(json_to_integer(json));
            break;
        case JSON_REAL:
            json_delete_real(json_to_real(json));
            break;
        default:
            return;  /* true, false, null はシングルトンなので解放しない */
    }
}
```

**設計ポイント:**
- `switch` による型ディスパッチ（vtable を使わない多態性の実現）
- 配列のデストラクタが子要素の `json_decref` を呼ぶ（再帰的なリソース解放）
- `default` でシングルトンが誤って解放されることを防止

---

## 3. BIO_METHOD vtable + ファクトリパターン — OpenSSL 3.0.16

I/O抽象化レイヤ（BIO）において、vtableパターンとファクトリパターンを組み合わせた高度な設計。
さらに参照カウント・スレッドロック・拡張データ機構を統合している。

### 3.1 不透明型の定義（typedef による情報隠蔽）

**ファイル:** `lib/external/openssl-3.0.16/include/openssl/types.h`

```c
typedef struct bio_st BIO;
typedef struct bignum_st BIGNUM;
typedef struct bignum_ctx BN_CTX;

typedef struct evp_cipher_st EVP_CIPHER;
typedef struct evp_cipher_ctx_st EVP_CIPHER_CTX;
typedef struct evp_md_st EVP_MD;
typedef struct evp_md_ctx_st EVP_MD_CTX;
typedef struct evp_mac_st EVP_MAC;
typedef struct evp_mac_ctx_st EVP_MAC_CTX;
typedef struct evp_pkey_st EVP_PKEY;

typedef struct ssl_st SSL;
typedef struct ssl_ctx_st SSL_CTX;
```

**設計ポイント:**
- 公開ヘッダではタグ名だけ宣言し、メンバは非公開（完全なカプセル化）
- ユーザは `BIO *` のようなポインタのみ扱い、構造体の中身にはアクセス不可

### 3.2 BIO_METHOD 仮想関数テーブル

**ファイル:** `lib/external/openssl-3.0.16/include/internal/bio.h`

```c
struct bio_method_st {
    int type;
    char *name;
    int (*bwrite) (BIO *, const char *, size_t, size_t *);
    int (*bwrite_old) (BIO *, const char *, int);
    int (*bread) (BIO *, char *, size_t, size_t *);
    int (*bread_old) (BIO *, char *, int);
    int (*bputs) (BIO *, const char *);
    int (*bgets) (BIO *, char *, int);
    long (*ctrl) (BIO *, int, long, void *);
    int (*create) (BIO *);
    int (*destroy) (BIO *);
    long (*callback_ctrl) (BIO *, int, BIO_info_cb *);
};
```

**設計ポイント:**
- `bwrite`/`bwrite_old` のように新旧APIを共存（後方互換性の維持）
- `create`/`destroy` で型固有の初期化・終了処理を定義
- `ctrl` は汎用制御関数（ioctl に類似した設計）

### 3.3 BIO_METHOD のファクトリと vtable セッター

**ファイル:** `lib/external/openssl-3.0.16/crypto/bio/bio_meth.c`

```c
/* BIO_METHOD のファクトリ関数 */
BIO_METHOD *BIO_meth_new(int type, const char *name)
{
    BIO_METHOD *biom = OPENSSL_zalloc(sizeof(BIO_METHOD));

    if (biom == NULL
            || (biom->name = OPENSSL_strdup(name)) == NULL) {
        OPENSSL_free(biom);
        ERR_raise(ERR_LIB_BIO, ERR_R_MALLOC_FAILURE);
        return NULL;
    }
    biom->type = type;
    return biom;
}

void BIO_meth_free(BIO_METHOD *biom)
{
    if (biom != NULL) {
        OPENSSL_free(biom->name);
        OPENSSL_free(biom);
    }
}

/* vtableの個別セッター — Builderパターンに近い */
int BIO_meth_set_write_ex(BIO_METHOD *biom,
                       int (*bwrite) (BIO *, const char *, size_t, size_t *))
{
    biom->bwrite_old = NULL;
    biom->bwrite = bwrite;
    return 1;
}

int BIO_meth_set_read_ex(BIO_METHOD *biom,
                         int (*bread) (BIO *, char *, size_t, size_t *))
{
    biom->bread_old = NULL;
    biom->bread = bread;
    return 1;
}

int BIO_meth_set_create(BIO_METHOD *biom, int (*create) (BIO *))
{
    biom->create = create;
    return 1;
}

int BIO_meth_set_destroy(BIO_METHOD *biom, int (*destroy) (BIO *))
{
    biom->destroy = destroy;
    return 1;
}
```

**設計ポイント:**
- vtable自体もオブジェクトとして動的に生成・設定（メタクラスパターン）
- セッター関数でフィールドを一つずつ設定（Builderパターン）
- 新APIのセッターを使うと旧APIのエントリポイントを自動的に無効化

### 3.4 BIO インスタンス構造体（内部実装）

**ファイル:** `lib/external/openssl-3.0.16/crypto/bio/bio_local.h`

```c
struct bio_st {
    OSSL_LIB_CTX *libctx;
    const BIO_METHOD *method;       /* vtableへのポインタ（const） */
    BIO_callback_fn_ex callback_ex;
    char *cb_arg;
    int init;
    int shutdown;
    int flags;
    int retry_reason;
    int num;
    void *ptr;                      /* 型固有のデータ（void *で汎用化） */
    struct bio_st *next_bio;        /* BIOチェーン */
    struct bio_st *prev_bio;
    CRYPTO_REF_COUNT references;    /* 参照カウント */
    uint64_t num_read;
    uint64_t num_write;
    CRYPTO_EX_DATA ex_data;         /* 拡張データ */
    CRYPTO_RWLOCK *lock;            /* スレッドロック */
};
```

**設計ポイント:**
- `method` が `const` で vtable の不変性を保証
- `void *ptr` で型固有データを格納（Strategyパターン）
- `next_bio`/`prev_bio` で BIO チェーン（Decorator パターン）を実現
- `CRYPTO_RWLOCK` でスレッドセーフ性を確保

### 3.5 BIO のコンストラクタ（vtable による初期化分岐）

**ファイル:** `lib/external/openssl-3.0.16/crypto/bio/bio_lib.c`

```c
BIO *BIO_new_ex(OSSL_LIB_CTX *libctx, const BIO_METHOD *method)
{
    BIO *bio = OPENSSL_zalloc(sizeof(*bio));

    if (bio == NULL) {
        ERR_raise(ERR_LIB_BIO, ERR_R_MALLOC_FAILURE);
        return NULL;
    }

    bio->libctx = libctx;
    bio->method = method;
    bio->shutdown = 1;
    bio->references = 1;

    if (!CRYPTO_new_ex_data(CRYPTO_EX_INDEX_BIO, bio, &bio->ex_data))
        goto err;

    bio->lock = CRYPTO_THREAD_lock_new();
    if (bio->lock == NULL) {
        ERR_raise(ERR_LIB_BIO, ERR_R_MALLOC_FAILURE);
        CRYPTO_free_ex_data(CRYPTO_EX_INDEX_BIO, bio, &bio->ex_data);
        goto err;
    }

    /* vtable に create 関数が登録されていれば呼び出す */
    if (method->create != NULL && !method->create(bio)) {
        ERR_raise(ERR_LIB_BIO, ERR_R_INIT_FAIL);
        CRYPTO_free_ex_data(CRYPTO_EX_INDEX_BIO, bio, &bio->ex_data);
        CRYPTO_THREAD_lock_free(bio->lock);
        goto err;
    }
    if (method->create == NULL)
        bio->init = 1;

    return bio;

err:
    OPENSSL_free(bio);
    return NULL;
}

BIO *BIO_new(const BIO_METHOD *method)
{
    return BIO_new_ex(NULL, method);
}
```

**設計ポイント:**
- `BIO_new_ex` / `BIO_new` でオーバーロード的なAPIを提供
- vtable の `create` が NULL の場合は自動的に `init = 1`（合理的なデフォルト）
- エラー時のリソース解放が `goto err` パターンで一元化

### 3.6 BIO のデストラクタ（参照カウント付き）

**ファイル:** `lib/external/openssl-3.0.16/crypto/bio/bio_lib.c`

```c
int BIO_free(BIO *a)
{
    int ret;

    if (a == NULL)
        return 0;

    if (CRYPTO_DOWN_REF(&a->references, &ret, a->lock) <= 0)
        return 0;

    REF_PRINT_COUNT("BIO", a);
    if (ret > 0)
        return 1;  /* まだ参照があるので解放しない */
    REF_ASSERT_ISNT(ret < 0);

    if (HAS_CALLBACK(a)) {
        ret = (int)bio_call_callback(a, BIO_CB_FREE, NULL, 0, 0, 0L, 1L, NULL);
        if (ret <= 0)
            return 0;
    }

    if ((a->method != NULL) && (a->method->destroy != NULL))
        a->method->destroy(a);  /* vtable のデストラクタを呼び出し */

    CRYPTO_free_ex_data(CRYPTO_EX_INDEX_BIO, a, &a->ex_data);

    CRYPTO_THREAD_lock_free(a->lock);

    OPENSSL_free(a);

    return 1;
}
```

**設計ポイント:**
- `CRYPTO_DOWN_REF` でアトミックに参照カウントをデクリメント
- コールバックで解放をフック可能（Observer パターン）
- vtable → 拡張データ → ロック → メモリの順で確実にリソースを解放

### 3.7 EVP コンテキストの生成・破棄

**ファイル:** `lib/external/openssl-3.0.16/crypto/evp/digest.c`

```c
EVP_MD_CTX *EVP_MD_CTX_new(void)
{
    return OPENSSL_zalloc(sizeof(EVP_MD_CTX));
}

void EVP_MD_CTX_free(EVP_MD_CTX *ctx)
{
    if (ctx == NULL)
        return;

    EVP_MD_CTX_reset(ctx);  /* 状態をリセットしてからメモリ解放 */
    OPENSSL_free(ctx);
}
```

**ファイル:** `lib/external/openssl-3.0.16/crypto/evp/evp_enc.c`

```c
EVP_CIPHER_CTX *EVP_CIPHER_CTX_new(void)
{
    return OPENSSL_zalloc(sizeof(EVP_CIPHER_CTX));
}

void EVP_CIPHER_CTX_free(EVP_CIPHER_CTX *ctx)
{
    if (ctx == NULL)
        return;
    EVP_CIPHER_CTX_reset(ctx);
    OPENSSL_free(ctx);
}
```

**設計ポイント:**
- `reset` と `free` を分離（状態のリセットだけで再利用可能にする設計）
- 同じパターンを `EVP_MD_CTX` と `EVP_CIPHER_CTX` で共有（一貫性）

### 3.8 SSL/SSL_CTX の階層的オブジェクト生成

**ファイル:** `lib/external/openssl-3.0.16/ssl/ssl_lib.c`

```c
/* SSL_CTX: 設定を保持するコンテキストオブジェクト */
SSL_CTX *SSL_CTX_new_ex(OSSL_LIB_CTX *libctx, const char *propq,
                        const SSL_METHOD *meth)
{
    SSL_CTX *ret = NULL;

    if (meth == NULL) {
        ERR_raise(ERR_LIB_SSL, SSL_R_NULL_SSL_METHOD_PASSED);
        return NULL;
    }

    if (!OPENSSL_init_ssl(OPENSSL_INIT_LOAD_SSL_STRINGS, NULL))
        return NULL;

    ret = OPENSSL_zalloc(sizeof(*ret));
    if (ret == NULL)
        goto err;

    /* ... 初期化コード（省略） ... */

    return ret;
 err:
    SSL_CTX_free(ret);
    return NULL;
}

SSL_CTX *SSL_CTX_new(const SSL_METHOD *meth)
{
    return SSL_CTX_new_ex(NULL, NULL, meth);
}

/* SSL: 個別接続のオブジェクト（SSL_CTX の設定を継承） */
SSL *SSL_new(SSL_CTX *ctx)
{
    SSL *s;

    if (ctx == NULL) {
        ERR_raise(ERR_LIB_SSL, SSL_R_NULL_SSL_CTX);
        return NULL;
    }
    if (ctx->method == NULL) {
        ERR_raise(ERR_LIB_SSL, SSL_R_SSL_CTX_HAS_NO_DEFAULT_SSL_VERSION);
        return NULL;
    }

    s = OPENSSL_zalloc(sizeof(*s));
    if (s == NULL)
        goto err;

    s->references = 1;
    s->lock = CRYPTO_THREAD_lock_new();

    /* コンテキストから設定を継承 */
    s->options = ctx->options;
    s->min_proto_version = ctx->min_proto_version;
    s->max_proto_version = ctx->max_proto_version;
    s->mode = ctx->mode;
    s->method = ctx->method;

    /* vtable の ssl_new で型固有の初期化 */
    if (!s->method->ssl_new(s))
        goto err;

    return s;

err:
    SSL_free(s);
    return NULL;
}
```

**設計ポイント:**
- `SSL_CTX` → `SSL` の二層構造（Factory + Prototype の組み合わせ）
- `SSL_METHOD` が vtable として機能（TLSバージョンごとの実装を切り替え）
- コンテキストの設定を新しい接続オブジェクトに自動継承
- `SSL_CTX_new_ex` / `SSL_CTX_new` で引数の多いバージョンと簡易バージョンを提供

---

## 4. 不透明ハンドル + マルチインスタンスパターン — libcurl 8.10.1

ビルド構成に応じて不透明度を切り替える二重定義パターンと、Easy/Multi の二層構造によるスケーラブルなインスタンス管理。

### 4.1 公開API vs 内部実装の二重定義

**ファイル:** `lib/external/curl-8.10.1/include/curl/curl.h`

```c
#if defined(BUILDING_LIBCURL) || defined(CURL_STRICTER)
typedef struct Curl_easy CURL;
typedef struct Curl_share CURLSH;
#else
typedef void CURL;
typedef void CURLSH;
#endif
```

```c
#if defined(BUILDING_LIBCURL) || defined(CURL_STRICTER)
typedef struct Curl_multi CURLM;
#else
typedef void CURLM;
#endif
```

**設計ポイント:**
- ライブラリ内部ではキャストなしで実際の構造体にアクセス可能
- ライブラリ外部では `void *` として完全に不透明
- `CURL_STRICTER` を定義すると型安全性を向上（開発時のミス検出）

### 4.2 Easy API（単一セッション管理）

**ファイル:** `lib/external/curl-8.10.1/include/curl/easy.h`

```c
CURL_EXTERN CURL *curl_easy_init(void);
CURL_EXTERN CURLcode curl_easy_setopt(CURL *curl, CURLoption option, ...);
CURL_EXTERN CURLcode curl_easy_perform(CURL *curl);
CURL_EXTERN void curl_easy_cleanup(CURL *curl);

CURL_EXTERN CURLcode curl_easy_getinfo(CURL *curl, CURLINFO info, ...);

CURL_EXTERN CURL *curl_easy_duphandle(CURL *curl);

CURL_EXTERN void curl_easy_reset(CURL *curl);
```

**設計ポイント:**
- `setopt`/`getinfo` の可変長引数でプロパティアクセスを統一（一種のリフレクション）
- `duphandle` でプロトタイプ複製パターン
- `reset` でオブジェクトの再初期化（`cleanup` + `init` を1回で実行）

### 4.3 コンストラクタの実装

**ファイル:** `lib/external/curl-8.10.1/lib/easy.c`

```c
struct Curl_easy *curl_easy_init(void)
{
    CURLcode result;
    struct Curl_easy *data;

    /* グローバルSSL初期化（一度だけ） */
    global_init_lock();

    if(!initialized) {
        result = global_init(CURL_GLOBAL_DEFAULT, TRUE);
        if(result) {
            DEBUGF(fprintf(stderr, "Error: curl_global_init failed\n"));
            global_init_unlock();
            return NULL;
        }
    }
    global_init_unlock();

    /* 内部構造体の生成 */
    result = Curl_open(&data);
    if(result) {
        DEBUGF(fprintf(stderr, "Error: Curl_open failed\n"));
        return NULL;
    }

    return data;
}
```

**設計ポイント:**
- ライブラリのグローバル初期化を遅延実行（lazy initialization）
- ロックによるスレッドセーフな初期化（Double-checked locking パターン）
- 実際のオブジェクト生成は `Curl_open` に委譲（責務の分離）

### 4.4 デストラクタの実装

**ファイル:** `lib/external/curl-8.10.1/lib/easy.c`

```c
void curl_easy_cleanup(struct Curl_easy *data)
{
    if(GOOD_EASY_HANDLE(data)) {
        SIGPIPE_VARIABLE(pipe_st);
        sigpipe_ignore(data, &pipe_st);
        Curl_close(&data);
        sigpipe_restore(&pipe_st);
    }
}
```

**設計ポイント:**
- `GOOD_EASY_HANDLE` マクロでマジックナンバーを検証（use-after-free の検出）
- SIGPIPE を一時的に無視（ネットワークプログラミングの定石）
- `Curl_close` がポインタのポインタを受け取り、解放後にNULLを設定

### 4.5 duphandle（プロトタイプ複製パターン）

**ファイル:** `lib/external/curl-8.10.1/lib/easy.c`

```c
struct Curl_easy *curl_easy_duphandle(struct Curl_easy *data)
{
    struct Curl_easy *outcurl = calloc(1, sizeof(struct Curl_easy));
    if(!outcurl)
        goto fail;

    outcurl->set.buffer_size = data->set.buffer_size;

    /* ユーザ設定をすべてコピー */
    if(dupset(outcurl, data))
        goto fail;

    Curl_dyn_init(&outcurl->state.headerb, CURL_MAX_HTTP_HEADER);

    outcurl->state.lastconnect_id = -1;
    outcurl->state.recent_conn_id = -1;
    outcurl->id = -1;

    outcurl->progress.flags    = data->progress.flags;
    outcurl->progress.callback = data->progress.callback;

#ifndef CURL_DISABLE_COOKIES
    if(data->cookies && data->state.cookie_engine) {
        outcurl->cookies = Curl_cookie_init(outcurl, NULL, outcurl->cookies,
                                            data->set.cookiesession);
        if(!outcurl->cookies)
            goto fail;
    }
#endif

    if(data->state.url) {
        outcurl->state.url = strdup(data->state.url);
        if(!outcurl->state.url)
            goto fail;
        outcurl->state.url_alloc = TRUE;
    }

    /* SSLエンジンの再初期化 */
    if(outcurl->set.str[STRING_SSL_ENGINE]) {
        if(Curl_ssl_set_engine(outcurl, outcurl->set.str[STRING_SSL_ENGINE]))
            goto fail;
    }

    /* ... 省略 ... */

    return outcurl;

fail:
    curl_easy_cleanup(outcurl);
    return NULL;
}
```

**設計ポイント:**
- 設定はコピーするが、接続状態やセッション情報はコピーしない（深いコピーと浅いコピーの使い分け）
- コンパイル時条件分岐（`#ifndef`）で無効化されたモジュールの処理をスキップ
- 失敗時は途中まで構築したオブジェクトを `curl_easy_cleanup` で確実に解放

### 4.6 Multi API（コンテナオブジェクト）

**ファイル:** `lib/external/curl-8.10.1/include/curl/multi.h`

```c
CURL_EXTERN CURLM *curl_multi_init(void);

CURL_EXTERN CURLMcode curl_multi_add_handle(CURLM *multi_handle,
                                            CURL *curl_handle);

CURL_EXTERN CURLMcode curl_multi_remove_handle(CURLM *multi_handle,
                                               CURL *curl_handle);

CURL_EXTERN CURLMcode curl_multi_perform(CURLM *multi_handle,
                                         int *running_handles);

CURL_EXTERN CURLMcode curl_multi_cleanup(CURLM *multi_handle);
```

**ファイル:** `lib/external/curl-8.10.1/lib/multi.c`

```c
CURLMcode curl_multi_add_handle(struct Curl_multi *multi,
                                struct Curl_easy *data)
{
    CURLMcode rc;
    if(!GOOD_MULTI_HANDLE(multi))
        return CURLM_BAD_HANDLE;

    if(!GOOD_EASY_HANDLE(data))
        return CURLM_BAD_EASY_HANDLE;

    /* 同じハンドルの二重追加を防止 */
    if(data->multi)
        return CURLM_ADDED_ALREADY;

    /* コールバック内からの再帰呼び出しを防止 */
    if(multi->in_callback)
        return CURLM_RECURSIVE_API_CALL;

    if(multi->dead) {
        if(multi->num_alive)
            return CURLM_ABORTED_BY_CALLBACK;
        multi->dead = FALSE;
    }

    /* easy_perform 用の内部multiハンドルがあれば解放 */
    if(data->multi_easy) {
        curl_multi_cleanup(data->multi_easy);
        data->multi_easy = NULL;
    }

    Curl_llist_init(&data->state.timeoutlist, NULL);
    /* ... */
}
```

**設計ポイント:**
- `CURLM` がコンテナ、`CURL` が要素という Composite パターン
- マジックナンバー検証で型の取り違えを実行時に検出
- 二重追加・再帰呼び出しなど不正操作を防止するガード条件

### 4.7 内部オブジェクト構造体

**ファイル:** `lib/external/curl-8.10.1/lib/urldata.h`

```c
struct Curl_easy {
    unsigned int magic;  /* マジックナンバーで型識別 */
    curl_off_t id;
    curl_off_t mid;      /* multi内でのID */

    struct connectdata *conn;
    struct Curl_llist_node multi_queue;  /* multi内リスト管理 */
    struct Curl_llist_node conn_queue;

    CURLMstate mstate;
    CURLcode result;

    struct Curl_message msg;
    struct easy_pollset last_poll;

    struct Names dns;
    struct Curl_multi *multi;        /* 所属するmultiハンドル */
    struct Curl_multi *multi_easy;   /* easy_perform用の内部multi */
    struct Curl_share *share;

    struct SingleRequest req;
    struct UserDefined set;          /* ユーザ設定値 */

#ifndef CURL_DISABLE_COOKIES
    struct CookieInfo *cookies;
#endif
    struct Progress progress;
    struct UrlState state;
    struct PureInfo info;
    /* ... */
};
```

**ファイル:** `lib/external/curl-8.10.1/lib/multihandle.h`

```c
struct Curl_multi {
    unsigned int magic;

    unsigned int num_easy;   /* 登録済みハンドル数 */
    unsigned int num_alive;  /* 完了前のハンドル数 */

    struct Curl_llist msglist;   /* 完了メッセージリスト */
    struct Curl_llist process;   /* 処理中 */
    struct Curl_llist pending;   /* 待機中 */
    struct Curl_llist msgsent;   /* 送信済み */
    curl_off_t next_easy_mid;

    curl_socket_callback socket_cb;
    void *socket_userp;

    curl_push_callback push_cb;
    void *push_userp;

    struct Curl_hash hostcache;  /* ホスト名キャッシュ */
    struct Curl_tree *timetree;  /* タイマー管理（スプレー木） */

    char *xfer_buf;
    size_t xfer_buf_len;

    struct Curl_hash sockhash;    /* ソケット→ハンドルのマッピング */
    struct Curl_hash proto_hash;  /* プロトコル実装のキャッシュ */
    struct cpool cpool;           /* 接続プール */

    long max_host_connections;
    long max_total_connections;

    curl_multi_timer_callback timer_cb;
    void *timer_userp;
    /* ... */
};
```

**設計ポイント:**
- `Curl_easy` と `Curl_multi` の両方にマジックナンバーを持ち、型安全性を実行時に担保
- 状態管理用のリスト（process, pending, msgsent）で状態遷移マシンを実現
- 接続プール（`cpool`）で HTTP Keep-Alive 接続の再利用を管理

---

## 5. 設定/接続分離パターン — s2n-tls

TLS設定（共有可能）と個別接続（一時的）を明確に分離。コールバック関数の登録で拡張性を確保。

### 5.1 不透明型の前方宣言

**ファイル:** `lib/include/s2n.h`

```c
struct s2n_config;
struct s2n_connection;
struct s2n_cert;
struct s2n_cert_chain_and_key;
typedef struct s2n_pkey s2n_cert_public_key;
typedef struct s2n_pkey s2n_cert_private_key;
```

### 5.2 コンストラクタ/デストラクタ群

**ファイル:** `lib/include/s2n.h`

```c
/* 設定オブジェクト */
S2N_API extern struct s2n_config *s2n_config_new(void);
S2N_API extern struct s2n_config *s2n_config_new_minimal(void);
S2N_API extern int s2n_config_free(struct s2n_config *config);
S2N_API extern int s2n_config_free_dhparams(struct s2n_config *config);
S2N_API extern int s2n_config_free_cert_chain_and_key(struct s2n_config *config);

/* 接続オブジェクト */
S2N_API extern struct s2n_connection *s2n_connection_new(s2n_mode mode);
S2N_API extern int s2n_connection_free(struct s2n_connection *conn);

/* 証明書チェーンオブジェクト */
S2N_API extern struct s2n_cert_chain_and_key *s2n_cert_chain_and_key_new(void);
S2N_API extern int s2n_cert_chain_and_key_load_pem(
    struct s2n_cert_chain_and_key *chain_and_key,
    const char *chain_pem,
    const char *private_key_pem);
```

**設計ポイント:**
- `s2n_config_new` と `s2n_config_new_minimal` でバリアントコンストラクタを提供
- `s2n_config_free_dhparams` のように部分的な解放関数も用意（リソースの段階的解放）
- `s2n_connection_new(s2n_mode mode)` で接続モード（サーバ/クライアント）を指定

### 5.3 コールバック登録パターン

**ファイル:** `lib/include/s2n.h`

```c
/* コールバック型定義 */
typedef int (*s2n_clock_time_nanoseconds)(void *, uint64_t *);
typedef int (*s2n_cache_retrieve_callback)(
    struct s2n_connection *conn, void *,
    const void *key, uint64_t key_size,
    void *value, uint64_t *value_size);
typedef int (*s2n_cache_store_callback)(
    struct s2n_connection *conn, void *,
    uint64_t ttl_in_seconds,
    const void *key, uint64_t key_size,
    const void *value, uint64_t value_size);
typedef int (*s2n_cache_delete_callback)(
    struct s2n_connection *conn, void *,
    const void *key, uint64_t key_size);

/* コールバック登録API */
S2N_API extern int s2n_config_set_wall_clock(
    struct s2n_config *config,
    s2n_clock_time_nanoseconds clock_fn,
    void *ctx);
S2N_API extern int s2n_config_set_cache_store_callback(
    struct s2n_config *config,
    s2n_cache_store_callback callback,
    void *data);
S2N_API extern int s2n_config_set_cache_retrieve_callback(
    struct s2n_config *config,
    s2n_cache_retrieve_callback callback,
    void *data);
```

**設計ポイント:**
- コールバックと `void *` コンテキストのペアで Strategy パターンを実現
- 設定オブジェクトにコールバックを登録し、接続オブジェクトで使用（設定と動作の分離）
- キャッシュ操作（store/retrieve/delete）を一式としてセットで登録

---

## 6. 階層的オブジェクトモデル — FreeType 2.3.5

Library → Face → Size → GlyphSlot という木構造でオブジェクトを階層管理。
`FT_Generic` による汎用データアタッチメント機構も特徴的。

### 6.1 ハンドル型定義の階層

**ファイル:** `lib/external/freetype-2.3.5/include/freetype/freetype.h`

```c
typedef struct FT_LibraryRec_  *FT_Library;
typedef struct FT_ModuleRec_*  FT_Module;
typedef struct FT_DriverRec_*  FT_Driver;
typedef struct FT_RendererRec_*  FT_Renderer;
typedef struct FT_FaceRec_*  FT_Face;
typedef struct FT_SizeRec_*  FT_Size;
typedef struct FT_GlyphSlotRec_*  FT_GlyphSlot;
typedef struct FT_CharMapRec_*  FT_CharMap;
```

**設計ポイント:**
- すべてのハンドルが `*Rec_` 構造体へのポインタ（`Rec` = Record の略、Pascalの慣習）
- ライブラリ → モジュール → ドライバ → レンダラーの階層が型名から読み取れる

### 6.2 汎用データアタッチメント（FT_Generic）

**ファイル:** `lib/external/freetype-2.3.5/include/freetype/fttypes.h`

```c
typedef void  (*FT_Generic_Finalizer)(void*  object);

typedef struct FT_Generic_ {
    void*                 data;
    FT_Generic_Finalizer  finalizer;
} FT_Generic;
```

**設計ポイント:**
- `void *data` と `finalizer` のペアでユーザ定義のデータ添付を実現
- `finalizer` がデストラクタの役割（RAII的なリソース管理）
- 各オブジェクトに `FT_Generic generic;` メンバを持たせて利用（拡張ポイントの提供）

### 6.3 ライブラリ/フェイスの生成・破棄

**ファイル:** `lib/external/freetype-2.3.5/include/freetype/freetype.h`

```c
/* ライブラリの初期化と終了 */
FT_EXPORT( FT_Error )
FT_Init_FreeType( FT_Library  *alibrary );

FT_EXPORT( FT_Error )
FT_Done_FreeType( FT_Library  library );

/* フォントフェイスの生成と破棄 */
FT_EXPORT( FT_Error )
FT_New_Face( FT_Library   library,
             const char*  filepathname,
             FT_Long      face_index,
             FT_Face     *aface );

FT_EXPORT( FT_Error )
FT_New_Memory_Face( FT_Library      library,
                    const FT_Byte*  file_base,
                    FT_Long         file_size,
                    FT_Long         face_index,
                    FT_Face        *aface );

FT_EXPORT( FT_Error )
FT_Done_Face( FT_Face  face );
```

**設計ポイント:**
- `FT_Init_` / `FT_Done_` でライブラリレベルの初期化と終了
- `FT_New_` / `FT_Done_` でオブジェクトの生成と破棄
- `FT_New_Face` と `FT_New_Memory_Face` でファクトリメソッドの多態性
- 出力ポインタを引数で受け取るパターン（エラーコードを戻り値で返すため）

### 6.4 オブジェクト階層の構造体定義

**ファイル:** `lib/external/freetype-2.3.5/include/freetype/freetype.h`

```c
typedef struct FT_SizeRec_ {
    FT_Face           face;       /* 親への参照 */
    FT_Generic        generic;    /* ユーザデータ */
    FT_Size_Metrics   metrics;
    FT_Size_Internal  internal;
} FT_SizeRec;

typedef struct FT_GlyphSlotRec_ {
    FT_Library        library;    /* ルートへの参照 */
    FT_Face           face;       /* 親への参照 */
    FT_GlyphSlot      next;       /* リンクリスト */
    FT_UInt           reserved;
    FT_Generic        generic;
    FT_Glyph_Metrics  metrics;
    FT_Fixed          linearHoriAdvance;
    FT_Fixed          linearVertAdvance;
    FT_Vector         advance;
    FT_Glyph_Format   format;
    FT_Bitmap         bitmap;
    FT_Int            bitmap_left;
    FT_Int            bitmap_top;
    FT_Outline        outline;
    FT_UInt           num_subglyphs;
    FT_SubGlyph       subglyphs;
    void*             control_data;
    long              control_len;
    FT_Pos            lsb_delta;
    FT_Pos            rsb_delta;
    void*             other;
    FT_Slot_Internal  internal;
} FT_GlyphSlotRec;
```

**設計ポイント:**
- 子オブジェクトが親への参照を保持（双方向ナビゲーション）
- `GlyphSlot` は `library`（ルート）と `face`（直接の親）の両方を参照
- すべてのオブジェクトに `FT_Generic generic` を持たせて拡張性を確保
- `next` ポインタによるリンクリストで同一フェイスの複数グリフスロットを管理

---

## 7. プラグイン/コールバックアーキテクチャ — libwebsockets 4.3.3

コンテキスト生成パラメータ構造体による設定注入と、プロトコルハンドラ・プラグインシステムによる動的拡張。

### 7.1 コンテキスト生成構造体

**ファイル:** `lib/external/libwebsockets-v4.3.3/include/libwebsockets/lws-context-vhost.h`

```c
struct lws_context_creation_info {
    const char *iface;
    const struct lws_protocols *protocols;
    const struct lws_extension *extensions;
    const struct lws_token_limits *token_limits;
    const char *http_proxy_address;
    const struct lws_protocol_vhost_options *headers;
    /* ... 多数のフィールド（省略） ... */
};
```

**設計ポイント:**
- 多数のパラメータを構造体にまとめ、コンストラクタ引数の爆発を回避
- ゼロ初期化で合理的なデフォルト値を使用（`calloc` / `memset` で初期化するだけでよい）

### 7.2 プロトコルハンドラ（Strategy パターン）

**ファイル:** `lib/external/libwebsockets-v4.3.3/include/libwebsockets/lws-protocols-plugins.h`

```c
struct lws_protocols {
    const char *name;
    lws_callback_function *callback;     /* プロトコル固有の処理 */
    size_t per_session_data_size;        /* セッションごとのデータサイズ */
    size_t rx_buffer_size;
    unsigned int id;
    void *user;
    size_t tx_packet_size;
};

/* 配列のターミネータ */
#define LWS_PROTOCOL_LIST_TERM { NULL, NULL, 0, 0, 0, NULL, 0 }
```

**設計ポイント:**
- `callback` にプロトコル固有の処理を登録（Strategy パターン）
- `per_session_data_size` でセッションごとのインスタンスデータを自動確保
- NULL終端の配列で複数プロトコルを登録（可変数の登録をシンプルに実現）

### 7.3 プラグインシステム

**ファイル:** `lib/external/libwebsockets-v4.3.3/include/libwebsockets/lws-protocols-plugins.h`

```c
/* プラグインヘッダ（すべてのプラグインに共通） */
typedef struct lws_plugin_header {
    const char *name;
    const char *_class;
    const char *lws_build_hash;  /* ビルドバージョン検証 */
    unsigned int api_magic;      /* APIバージョン検証 */
} lws_plugin_header_t;

/* プロトコル型プラグイン */
typedef struct lws_plugin_protocol {
    lws_plugin_header_t hdr;
    const struct lws_protocols *protocols;
    const struct lws_extension *extensions;
    int count_protocols;
    int count_extensions;
} lws_plugin_protocol_t;

/* プラグイン管理（リンクリスト） */
struct lws_plugin {
    struct lws_plugin *list;
    const lws_plugin_header_t *hdr;
    union {
        uv_lib_t lib;
        void *l;
    } u;
};

/* プラグインのライフサイクル管理 */
LWS_VISIBLE LWS_EXTERN int
lws_plugins_init(struct lws_plugin **pplugin, const char * const *d,
                 const char *_class, const char *filter,
                 each_plugin_cb_t each, void *each_user);

LWS_VISIBLE LWS_EXTERN int
lws_plugins_destroy(struct lws_plugin **pplugin, each_plugin_cb_t each,
                    void *each_user);
```

**設計ポイント:**
- `api_magic` と `lws_build_hash` でABI互換性を実行時にチェック
- `lws_plugin_header_t` を共通ヘッダとし、プラグイン種別ごとに拡張（libnl の `NLHDR_COMMON` と同様の発想）
- `each_plugin_cb_t` コールバックでプラグイン列挙時の処理をカスタマイズ可能

---

## 8. ポリシーベースオブジェクト生成 — libsrtp2

ポリシー構造体で暗号設定を記述し、セッション生成時に注入する設計。

### 8.1 不透明セッション型

**ファイル:** `lib/include/srtp2/srtp.h`

```c
typedef struct srtp_ctx_t_ srtp_ctx_t;
typedef srtp_ctx_t *srtp_t;
```

### 8.2 ポリシー構造体による設定注入

**ファイル:** `lib/include/srtp2/srtp.h`

```c
typedef struct srtp_crypto_policy_t {
    srtp_cipher_type_id_t cipher_type;
    int cipher_key_len;
    srtp_auth_type_id_t auth_type;
    int auth_key_len;
    int auth_tag_len;
    srtp_sec_serv_t sec_serv;
} srtp_crypto_policy_t;

typedef struct srtp_policy_t {
    srtp_ssrc_t ssrc;
    srtp_crypto_policy_t rtp;          /* RTP用暗号ポリシー */
    srtp_crypto_policy_t rtcp;         /* RTCP用暗号ポリシー */
    unsigned char *key;
    srtp_master_key_t **keys;
    unsigned long num_master_keys;
    void *deprecated_ekt;
    unsigned long window_size;
    int allow_repeat_tx;
    int *enc_xtn_hdr;
    int enc_xtn_hdr_count;
    struct srtp_policy_t *next;        /* 複数ポリシーのチェーン */
} srtp_policy_t;
```

**設計ポイント:**
- `srtp_crypto_policy_t` を RTP と RTCP で共有（コンポジションによる再利用）
- `next` ポインタでポリシーをチェーン（複数SSRCへの一括設定）
- ポリシーとセッションを分離し、同一ポリシーで複数セッションを生成可能

### 8.3 ライブラリ初期化とセッション管理

**ファイル:** `lib/include/srtp2/srtp.h`

```c
/* ライブラリのグローバル初期化/終了 */
srtp_err_status_t srtp_init(void);
srtp_err_status_t srtp_shutdown(void);

/* セッションの生成/破棄 */
srtp_err_status_t srtp_create(srtp_t *session,
                              const srtp_policy_t *policy);

srtp_err_status_t srtp_dealloc(srtp_t s);
```

**設計ポイント:**
- `srtp_create` が出力ポインタとポリシーを受け取る（Factory Method パターン）
- エラーコードを戻り値で返し、オブジェクトはポインタ引数で返す（C言語の慣例）
- `init`/`shutdown` でグローバル状態の管理を明示

---

## 9. 設計パターン横断比較表

| パターン | libnl | Jansson | OpenSSL | curl | s2n | FreeType | lws | srtp |
|----------|:-----:|:-------:|:-------:|:----:|:---:|:--------:|:---:|:----:|
| vtable（関数ポインタテーブル） | **○** | - | **○** | - | - | - | - | - |
| 不透明ハンドル | ○ | ○ | **○** | **○** | **○** | **○** | ○ | ○ |
| 参照カウント | **○** | **○** | **○** | - | - | - | - | - |
| container_of 継承 | **○** | **○** | - | - | - | - | - | - |
| シングルトン | - | **○** | - | - | - | - | - | - |
| ファクトリメソッド | ○ | ○ | **○** | ○ | ○ | **○** | ○ | ○ |
| プロトタイプ複製 | **○** | - | - | **○** | - | - | - | - |
| Builder / 設定注入 | - | - | **○** | - | **○** | - | **○** | **○** |
| Composite / コンテナ | **○** | ○ | **○** | **○** | - | **○** | - | - |
| Strategy / コールバック | ○ | - | ○ | ○ | **○** | ○ | **○** | - |
| プラグインシステム | - | - | - | - | - | **○** | **○** | - |
| マジックナンバー検証 | - | - | - | **○** | - | - | - | - |
| ABI予約スロット | **○** | - | - | - | - | - | **○** | - |
| アトミック操作 | - | **○** | **○** | - | - | - | - | - |
| 階層的オブジェクト | **○** | - | **○** | **○** | ○ | **○** | ○ | - |

**凡例:** **○** = 特に優れた実装 / ○ = 利用あり / - = 未使用

---

## 参考ファイル一覧

| ライブラリ | 主要ファイル |
|-----------|-------------|
| libnl 3.2.27 | `lib/external/libnl-3.2.27/include/netlink-private/object-api.h` |
| | `lib/external/libnl-3.2.27/lib/object.c` |
| | `lib/external/libnl-3.2.27/lib/socket.c` |
| | `lib/external/libnl-3.2.27/include/netlink-private/types.h` |
| | `lib/external/libnl-3.2.27/include/netlink-private/cache-api.h` |
| Jansson 2.14 | `lib/external/jansson-2.14/src/jansson.h` |
| | `lib/external/jansson-2.14/src/jansson_private.h` |
| | `lib/external/jansson-2.14/src/value.c` |
| OpenSSL 3.0.16 | `lib/external/openssl-3.0.16/include/openssl/types.h` |
| | `lib/external/openssl-3.0.16/include/internal/bio.h` |
| | `lib/external/openssl-3.0.16/crypto/bio/bio_lib.c` |
| | `lib/external/openssl-3.0.16/crypto/bio/bio_meth.c` |
| | `lib/external/openssl-3.0.16/crypto/bio/bio_local.h` |
| | `lib/external/openssl-3.0.16/crypto/evp/digest.c` |
| | `lib/external/openssl-3.0.16/crypto/evp/evp_enc.c` |
| | `lib/external/openssl-3.0.16/ssl/ssl_lib.c` |
| libcurl 8.10.1 | `lib/external/curl-8.10.1/include/curl/curl.h` |
| | `lib/external/curl-8.10.1/include/curl/easy.h` |
| | `lib/external/curl-8.10.1/include/curl/multi.h` |
| | `lib/external/curl-8.10.1/lib/easy.c` |
| | `lib/external/curl-8.10.1/lib/multi.c` |
| | `lib/external/curl-8.10.1/lib/urldata.h` |
| | `lib/external/curl-8.10.1/lib/multihandle.h` |
| s2n-tls | `lib/include/s2n.h` |
| FreeType 2.3.5 | `lib/external/freetype-2.3.5/include/freetype/freetype.h` |
| | `lib/external/freetype-2.3.5/include/freetype/fttypes.h` |
| libwebsockets 4.3.3 | `lib/external/libwebsockets-v4.3.3/include/libwebsockets/lws-context-vhost.h` |
| | `lib/external/libwebsockets-v4.3.3/include/libwebsockets/lws-protocols-plugins.h` |
| libsrtp2 | `lib/include/srtp2/srtp.h` |

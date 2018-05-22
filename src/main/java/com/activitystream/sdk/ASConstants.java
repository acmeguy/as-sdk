package com.activitystream.sdk;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ASConstants {

    //ParentClass for most anything
    public static final String AS_STREAM_ITEM = "ASStreamItem";
    public static final String API_MESSAGE_PREFIX = "as.api.";

    /**
     * SETTINGS
     **/
    public static final String ACTIVE_SETTINGS = "_active_settings";

    public static final String MESSAGE_TYPE_SIGNATURE_ENTITY = API_MESSAGE_PREFIX + "entity";

    /**
     * FIELD NAMES
     **/

    public static final String FIELD_ENTITY_TYPE = "entity_type";
    public static final String FIELD_ENTITY_REF = "entity_ref";
    public static final String FIELD_ENTITY_ID = "entity_id";
    public static final String FIELD_ID = "id";
    public static final String FIELD_TYPE_REF = "type_ref";
    public static final String FIELD_EXTERNAL_ID = "external_id";
    public static final String FIELD_BATCH_ID = "batch_id";
    public static final String FIELD_STREAM_ID = "stream_id";
    public static final String FIELD_STREAM_ID_INTERNAL = "_stream_id";
    public static final String FIELD_OCCURRED_AT = "occurred_at";
    public static final String FIELD_FIRST_SEEN = "first_seen";
    public static final String FIELD_LAST_SEEN = "last_seen";
    public static final String FIELD_UPDATED_AT = "updated_at";
    public static final String FIELD_OCCURRED_AT_LTOD = "occurred_at_ltod";
    public static final String FIELD_ORIGIN = "origin";
    public static final String FIELD_PARTITION = "partition";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_INVOLVES = "involves";
    public static final String FIELD_RELATIONS = "relations";
    public static final String FIELD_ASPECTS = "aspects";
    public static final String FIELD_POSTAL_CODE = "postal_code";
    public static final String FIELD_ASPECT_SUPPORT = "aspect_support";
    public static final String FIELD_SUPPORTED_ENRICHMENTS = "supported_enrichments";
    public static final String FIELD_ACTIVE_VALIDATIONS = "active_validations";
    public static final String FIELD_ARCHETYPE = "archetype";
    public static final String FIELD_ARCHETYPE_VARIANT = "archetype_variant";
    public static final String FIELD_COMMON = "common";
    public static final String FIELD_SUBJECT = "subject";
    public static final String FIELD_PROPERTIES = "properties";
    public static final String FIELD_IMPORTANCE = "importance";
    public static final String FIELD_ACL = "acl";
    public static final String FIELD_RECEIVED_AT = "received_at";
    public static final String FIELD_REGISTERED_AT = "registered_at";
    public static final String FIELD_TOKEN = "token";
    public static final String FIELD_ACCESS_TOKEN = "access_token";
    public static final String FIELD_ACCESS_TYPE = "access_type";
    public static final String FIELD_CONFIG = "config";
    public static final String FIELD_GRANULARITY = "granularity";
    public static final String FIELD_IMP_SPECIFIC = "imp_specific";

    public static final String FIELD_METRICS_SEEN = "metrics_seen";
    public static final String FIELD_DIMENSIONS_SEEN = "dimensions_seen";
    public static final String FIELD_SEEN_COUNT = "seen_count";
    public static final String FIELD_TIME_SERIES_TYPE = "time_series_type";
    public static final String FIELD_TIME_SERIES = "time_series";

    public static final String FIELD_GROUP_ON = "group_on";
    public static final String FIELD_COLLAPSE_ON = "collapse_on";
    public static final String FIELD_RETENTION = "retention";
    public static final String FIELD_ACTIVE = "active";
    public static final String FIELD_LAST_CHECKED = "last_checked";
    public static final String FIELD_PRIVATE = "private";
    public static final String FIELD_TODO_LIST = "todo_list";
    public static final String FIELD_ANALYSE = "analyse";
    public static final String FIELD_RENDERING = "rendering";
    public static final String FIELD_BROADCAST = "broadcast";
    public static final String FIELD_BUMP = "bump";
    public static final String FIELD_GROUP = "group";
    public static final String FIELD_COLLAPSE = "collapse";
    public static final String FIELD_ENRICH = "enrich";
    public static final String FIELD_RETURN_LOOP = "return_loop";
    public static final String FIELD_STATISTICS = "statistics";
    public static final String FIELD_STORE = "store";
    public static final String FIELD_COMMENT = "comment";
    public static final String FIELD_CONTENT = "content";
    public static final String FIELD_CONTEXT = "context";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_SUBTITLE = "subtitle";
    public static final String FIELD_BYLINE = "byline";
    public static final String FIELD_DETECTED_LANGUAGE = "detected_language";
    public static final String FIELD_SENTIMENT = "sentiment";
    public static final String FIELD_SIGNATURE = "signature";
    public static final String FIELD_TEMPLATES = "templates";

    public static final String FIELD_URL = "url";
    public static final String FIELD_FILENAME = "filename";
    public static final String FIELD_FINGERPRINT = "fingerprint";
    public static final String FIELD_SIZE = "size";
    public static final String FIELD_HEIGHT = "height";
    public static final String FIELD_WIDTH = "width";
    public static final String FIELD_BITRATE = "bitrate";
    public static final String FIELD_CONTENT_TYPE = "content_type";
    public static final String FIELD_CREATED = "created";
    public static final String FIELD_UPDATED = "updated";
    public static final String FIELD_METADATA = "metadata";

    public static final String FIELD_PRICE_CATEGORY = "price_category";
    public static final String FIELD_PRICE_TYPE = "price_type";
    public static final String FIELD_ACCOUNTING_KEY = "accounting_key";
    public static final String FIELD_LOCALE = "locale";
    public static final String FIELD_CURRENCY = "currency";
    public static final String FIELD_VARIANT = "variant";
    public static final String FIELD_PAYMENT_METHOD = "payment_method";
    public static final String FIELD_CARD_TOKEN = "card_token";
    public static final String FIELD_SERIAL_NUMBERS = "serial_numbers";
    public static final String FIELD_LINE_IDS = "line_ids";
    public static final String FIELD_TRANSACTION_TYPE = "transaction_type";
    public static final String FIELD_PRODUCT = "product";
    public static final String FIELD_ITEM_COUNT = "item_count";
    public static final String FIELD_COMPLIMENTARY = "complimentary";
    public static final String FIELD_ITEM_PRICE = "item_price";
    public static final String FIELD_ITEMS_FOR_SALE = "items_for_sale";
    public static final String FIELD_ITEMS_IN_STOCK = "items_in_stock";
    public static final String FIELD_ITEMS_ON_HOLD = "items_on_hold";
    public static final String FIELD_ITEMS_UNSELLABLE = "items_unsellable";
    public static final String FIELD_ITEMS_RESERVED = "items_reserved";
    public static final String FIELD_ITEMS_SOLD = "items_sold";
    public static final String FIELD_ITEMS_COMPLIMENTARY = "items_complimentary";
    public static final String FIELD_ITEMS_RETURNED = "items_returned";
    public static final String FIELD_GROSS_SOLD = "gross_sold";
    public static final String FIELD_PRICE_CATEGORIES = "price_categories";
    public static final String FIELD_VARIANTS = "variants";
    public static final String FIELD_COMMISSION_FIXED = "commission_fixed";
    public static final String FIELD_COMMISSION_PERCENTAGE = "commission_percentage";
    public static final String FIELD_DISCOUNT_PERCENTAGE = "discount_percentage";
    public static final String FIELD_TAX_PERCENTAGE = "tax_percentage";
    public static final String FIELD_TAGS = "tags";
    public static final String FIELD_TOTAL_IN_STOCK = "total_in_stock";
    public static final String FIELD_TOTAL_FOR_SALE = "total_for_sale";
    public static final String FIELD_VALIDATE = "validate";
    public static final String FIELD_VALID_FROM = "valid_from";
    public static final String FIELD_VALID_UNTIL = "valid_until";
    public static final String FIELD_DEFAULTS = "defaults";

    public static final String FIELD_DELETE = "$delete";

    /**
     * ASPECTS
     **/
    public static final String ASPECTS_DIMENSIONS = "dimensions";
    public static final String ASPECTS_METRICS = "metrics";
    public static final String ASPECTS_MESSAGING = "messaging";
    public static final String ASPECTS_CLASSIFICATION = "classification";
    public static final String ASPECTS_ADDRESS = "address";
    public static final String ASPECTS_TAGS = "tags";
    public static final String ASPECTS_PRESENTATION = "presentation";
    public static final String ASPECTS_GEO_LOCATION = "geo_location";
    public static final String ASPECTS_ATTACHMENTS = "attachments";
    public static final String ASPECTS_CONTEXT = "context";
    public static final String ASPECTS_OBS_EVENTS = "obs_events";
    public static final String ASPECTS_AB_TEST = "ab_test";
    public static final String ASPECTS_CLIENT_DEVICE = "client_device";
    public static final String ASPECTS_CLIENT_IP = "client_ip";
    public static final String ASPECTS_INVENTORY = "inventory";
    public static final String ASPECTS_CONTENT = "content";
    public static final String ASPECTS_TS_DATA = "ts_data";
    public static final String ASPECTS_TIMED = "timed";
    public static final String ASPECTS_STATUS = "status";
    public static final String ASPECTS_LOCALE = "locale";
    public static final String ASPECTS_IDENTIFIABLE = "identifiable";
    public static final String ASPECTS_SETTINGS = "settings";
    public static final String ASPECTS_PRODUCT_VIEW = "productview";
    public static final String ASPECTS_TRAFFIC_SOURCE = "traffic_source";
    public static final String ASPECTS_TRAFFIC_SOURCES = "traffic_sources";
    public static final String ASPECTS_GROUPED = "grouped";
    public static final String ASPECTS_CEI = "cei";
    public static final String ASPECTS_DEMOGRAPHY = "demography";
    public static final String ASPECTS_ITEMS = "items";
    public static final String ASPECTS_RESOLVABLE = "resolvable";
    public static final String ASPECTS_LINK = "link";

    /**
     * ASPECT FIELDS
     **/
    public static final String FIELD_LATLONG = "latlong";
    public static final String FIELD_LATITUDE = "latitude";
    public static final String FIELD_LONGITUDE = "longitude";
    public static final String FIELD_TRACK_FOR = "track_for";
    public static final String FIELD_HASC_CODE = "hasc";
    public static final String FIELD_FIPS_CODE = "fips";
    public static final String FIELD_NGA_CODE = "nga";
    public static final String FIELD_COUNTRY_CODE = "country_code";
    public static final String FIELD_TIMEZONE = "timezone";
    public static final String FIELD_VERIFIED = "verified";

    public static final String FIELD_GENDER = "gender";
    public static final String FIELD_GENDER_GUESSED = "gender_guessed";

    public static final String FIELD_CAMPAIGN = "campaign";
    public static final String FIELD_SOURCE = "source";
    public static final String FIELD_MEDIUM = "medium";

    public static final String FIELD_OUTCOME = "outcome";
    public static final String FIELD_METRIC = "metric";
    public static final String FIELD_AMOUNT = "amount";

    public static final String FIELD_ENGAGEMENT = "engagement";
    public static final String FIELD_HAPPINESS = "happiness";
    public static final String FIELD_CARE = "care";
    public static final String FIELD_RATING = "rating";
    public static final String FIELD_INTENT = "intent";
    public static final String FIELD_DURATION = "duration";
    public static final String FIELD_FLAGGED = "flagged";

    public static final String FIELD_BEGINS = "begins";
    public static final String FIELD_ENDS = "ends";

    public static final String FIELD_IP = "ip";

    public static final String FIELD_LABEL = "label";
    public static final String FIELD_DETAILS_URL = "details_url";
    public static final String FIELD_THUMBNAIL = "thumbnail";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_ICON = "icon";
    public static final String FIELD_CATEGORIES = "categories";
    public static final String FIELD_CATEGORY = "category";
    public static final String FIELD_SUB_CATEGORY = "sub_category";
    public static final String FIELD_LAST_UPDATED = "last_updated";
    public static final String FIELD_OUTLOOK = "outlook";
    public static final String FIELD_RANKING = "ranking";
    public static final String FIELD_WEIGHT = "weight";
    public static final String FIELD_TRUSTED = "trusted";
    public static final String FIELD_ACTIVE_SINCE = "active_since";
    public static final String FIELD_ACTIVE_FROM = "active_from";
    public static final String FIELD_ACTIVE_UNTIL = "active_until";
    public static final String FIELD_TERM = "term";
    public static final String FIELD_REFERRER = "referrer";
    public static final String FIELD_SECTION = "section";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_PATH = "path";
    public static final String FIELD_METHOD = "method";

    public static final String FIELD_TENANT_LABEL = "tenant_label";

    public static final String FIELD_BIRTH_YEAR = "birth_year";
    public static final String FIELD_BIRTH_MONTH = "birth_month";
    public static final String FIELD_BIRTH_DAY = "birth_day";
    public static final String FIELD_FAMILY_SIZE = "family_size";
    public static final String FIELD_BIRTH_DATE = "birth_date";

    public static final String FIELD_ZIP_CODE = "zip_code";
    public static final String FIELD_CITY = "city";
    public static final String FIELD_MUNICIPALITY = "municipality";
    public static final String FIELD_COUNTRY = "country";
    public static final String FIELD_ADDRESS = "address";
    public static final String FIELD_ADDRESS_2 = "address2";
    public static final String FIELD_STATE = "state";
    public static final String FIELD_STATE_CODE = "state_code";
    public static final String FIELD_REGION = "region";
    public static final String FIELD_ZIP_LATLONG = "zip_latlong";
    public static final String FIELD_HASC = "hasc";
    public static final String FIELD_SUB_REGION = "sub_region";
    public static final String FIELD_ALTERED = "altered";

    public static final String FIELD_ETHNICITY = "ethnicity";
    public static final String FIELD_MARITAL_STATUS = "marital_status";
    public static final String FIELD_EMPLOYMENT = "employment";
    public static final String FIELD_INCOME = "income";
    public static final String FIELD_HOUSING = "housing";
    public static final String FIELD_MOSAIC_GROUP = "mosaic_group";
    public static final String FIELD_EDUCATION = "education";

    public static final String FIELD_USER_AGENT = "user_agent";

    public static final String FIELD_VERSION = "version";
    public static final String FIELD_LOOKS_INVALID = "looks_invalid";
    public static final String FIELD_UPDATE_OCCURRED_AT = "update_occurred_at";

    public static final String FIELD_INHERITED_VIA = "_inherited_via";
    public static final String FIELD_MERGED_FIELDS = "_merged_fields";

    public static final String TIME_SERIES_EVENTS = "events";  //to-be-depricated
    public static final String TIME_SERIES_BAMM = "bamm";
    public static final String TIME_SERIES_TRANSACTIONS = "transactions";
    public static final String TIME_SERIES_GENERIC = "timeseries";
    public static final String TIME_SERIES_ENTITY_SAMPLES = "entitysamples";
    public static final String TIME_SERIES_METRICS = "metrics";
    public static final String TIME_SERIES_INVENTORY = "inventory";

    public static final String ARCHETYPE_CUSTOMER = "Customer";
    public static final String ARCHETYPE_PRODUCT = "Product";
    public static final String ARCHETYPE_PRODUCTGROUP = "Product Group";

    public static final List<String> ALL_ASPECT_FIELDS =
            Arrays.asList(ASPECTS_CLASSIFICATION, ASPECTS_DIMENSIONS, ASPECTS_AB_TEST, ASPECTS_ADDRESS, ASPECTS_ATTACHMENTS, ASPECTS_CEI, ASPECTS_CLIENT_DEVICE,
                    ASPECTS_CLIENT_IP, ASPECTS_RESOLVABLE, ASPECTS_CONTENT, ASPECTS_DEMOGRAPHY, ASPECTS_GEO_LOCATION, ASPECTS_ITEMS, ASPECTS_LINK, ASPECTS_LOCALE,
                    ASPECTS_GEO_LOCATION, ASPECTS_METRICS);



    //Event Base
    public static final String AS_EVENT = "ASEvent";
    public static final String AS_CUSTOMER_EVENT = "ASCustomerEvent";
    public static final String AS_TRANSACTION_EVENT = "ASTransactionEvent";
    public static final String AS_OBSERVATION = "ASObservation";
    public static final String IDX_OBSERVATION_DATE = "idx_ObservationDate";
    public static final String IDX_OBSERVATION_DATE_UP = "idx_ObservationDateUpdated";

    // Basic Event information
    public static final String AS_INTELLIGENCE = "ASIntelligence";
    public static final String AS_ORIGIN = "ASOrigin";
    public static final String AS_PARTITION = "ASPartition";
    public static final String AS_EVENT_TYPE = "ASEventType";
    public static final String AS_ENTITY_TYPE = "ASEntityType";
    public static final String AS_RELATIONS_TYPE = "ASRelationsType";

    /* Reusable Aspect Value Entities */
    public static final String AS_DV_DIMENSIONS = "ASDimensionValues";
    public static final String AS_DV_METRICS = "ASMetricValues";
    public static final String AS_DV_CLASSIFICATION = "ASClassificationValues";
    public static final String AS_DV_DEMOGRAPHY = "ASDemographyValues";
    public static final String AS_DV_SETTINGS = "ASSettingsValues";
    public static final String AS_DV_PRESENTATION = "ASPresentationValues";

    public static final String AS_ENTITY = "ASEntity";

    public static final String AS_TRACKED_ASPECT = "ASTrackedAspect";
    public static final String AS_CUSTOMER_ENTITY = "ASCustomerEntity";
    public static final String AS_COMMENT = "ASComment";
    public static final String AS_CHANNEL = "ASChannel";
    public static final String AS_TOKEN = "ASToken";
    public static final String AS_SHARED_CONFIG = "ASSharedConfig";
    public static final String IDX_EXT_TOKEN = "idx_ExtToken";
    public static final String IDX_EXT_SHARED_CONFIG = "idx_ExtSharedConfig";
    public static final String AS_INTERNAL_ENTITY = "ASInternal";

    /***
     *
     * Relation Types
     *
     */

    public static final String REL_BASE = "RELATIONS";
    public static final String REL_ENTITY_RELATIONS = "ENTITY_RELATIONS";

    /* Access Relations */
    public static final String REL_ACL_RELATIONS = "ACL_RELATIONS";
    public static final String REL_ACL_READ = "READ";
    public static final String REL_ACL_MANAGE = "MANAGE";

    /* Base Relations */
    public static final String REL_IS = "IS";                 //exclusive 1:1 relations (Base)
    public static final String REL_INTEGRAL_TO = "INTEGRAL_TO";        //exclusive 1:1 relations (Base)
    public static final String REL_CLOSE = "CLOSE";              //semi-exclusive n:1 close-relations (Base)
    public static final String REL_KNOWS = "KNOWS";              //knows of it's existence
    public static final String REL_KNOWS_OF = "KNOWS_OF";           //Should know of it's existence

    /* Entity Relations */
    public static final String REL_ALIAS = "ALIAS";                 //exclusive 1:1 relations (IS) (private email/mobile phone)
    public static final String REL_AKA = "AKA";                     //exclusive 1:1 relations (IS)
    public static final String REL_SOCIAL = "SOCIAL_ID";            //exclusive 1:1 relations (IS)
    public static final String REL_PART_OF = "PART_OF";             //exclusive 1:n relations (Membership, employment) (CLOSE) //Contains
    public static final String REL_MEMBER_OF = "MEMBER_OF";         //exclusive 1:n relations (Membership) (KNOWS) //Contains
    public static final String REL_VERSION_OF = "VERSION_OF";       //exclusive 1:n relations (Membership, employment) (CLOSE) //Contains
    public static final String REL_PAIRED_WITH = "PAIRED_WITH";     //non-exclusive 1:n relations (Membership, employment) (CLOSE) //Contains
    public static final String REL_MEASURES = "MEASURES";           //non-exclusive 1:n relations (Membership, employment) (CLOSE) //Contains
    public static final String REL_RATED_BY = "RATED_BY";           //non-exclusive 1:n relations (Price calculations) (CLOSE)
    public static final String REL_POWERED_BY = "POWERED_BY";       //non-exclusive 1:n relations (Membership, employment) (CLOSE) //Contains
    public static final String REL_BELONGS_TO = "BELONGS_TO";       //non-exclusive 1:n relations (describes ownership/possession) (CLOSE)
    public static final String REL_LOCATED_AT = "LOCATED_AT";       //Is Temporarily located at (KNOWS)
    public static final String REL_LOCATED_IN = "LOCATED_IN";       //Is permanently located in (CLOSE)
    public static final String REL_PROXY_FOR = "PROXY_FOR";         //exclusive 1:1 relations from a Business Entity To a Extension (Like a WebSession) (CLOSE)
    public static final String REL_ON_BEHALF_OF = "ON_BEHALF_OF";   //non-exclusive n:1 relations
    public static final String REL_RELAYED_BY = "RELAYED_BY";       //vague n:1 relations (employee on behalf of a customer... for example)
    public static final String REL_ASSOCIATED_WITH = "ASSOCIATED_WITH";    //Has social relations to (less important than family)
    public static final String REL_RELATED_TO = "RELATED_TO";       //family relations
    public static final String REL_HAS_RELATIONS_TO = "HAS_RELATIONS_TO";   //Has other relations to (less important than family)
    public static final String REL_OF_TYPE = "OF_TYPE";             //The entity is of a particulate type (eg. Make, Model, Year)
    public static final String REL_HOSTED_AT = "HOSTED_AT";         //Has social relations to (less important than family)

    public static final String REL_STARS_IN = "STARS_IN";
    public static final String REL_INSTRUMENTAL_IN = "INSTRUMENTAL_IN";
    public static final String REL_APPEARS_IN = "APPEARS_IN";
    public static final String REL_ASSISTS_IN = "ASSISTS_IN";

    /* Sub Events */
    public static final String REL_SUB_EVENT = "SUB_EVENT";

    /* Event Relations */
    public static final String REL_EVENT_RELATIONS = "EVENT_RELATIONS";
    public static final String REL_ACTOR = "ACTOR";              //The Entity responsible for causing the event
    public static final String REL_AFFECTS = "AFFECTS";            //Event affects the referenced Entity
    public static final String REL_INVOLVES = "INVOLVES";           //Entity is involved in event (not directly affected)
    public static final String REL_OBSERVES = "OBSERVES";           //Entity is witnesses/observes the event (not directly affected)
    public static final String REL_REFERENCES = "REFERENCES";         //Entity is referenced in the event (not involved or directly affected)
    public static final String REL_BUYER = "BUYER";

    /* Event Relations + commerce */
    public static final String REL_DELIVERED_TO = "DELIVERED_TO";
    public static final String REL_BOUGHT_FOR = "BOUGHT_FOR";
    public static final String REL_SERIAL = "SERIAL_NUMBER";
    public static final String REL_TRANSACTION = "TRANSACTION";
    public static final String REL_SUPPLIED_BY = "SUPPLIED_BY";        //non-exclusive n:n relations (KNOWS)
    public static final String REL_MANUFACTURED_BY = "MANUFACTURED_BY";    //non-exclusive n:n relations (KNOWS)
    public static final String REL_SOLD_BY = "SOLD_BY";
    public static final String REL_GRANTS_ACCES_TO = "GRANTS_ACCESS_TO";

    /* Event Relations + lifecycle */
    public static final String REL_CREATES = "CREATES";            //Event creates the referenced Entity
    public static final String REL_UPDATES = "UPDATES";            //Event updates the referenced Entity
    public static final String REL_REMOVES = "REMOVES";            //Event removes/deletes/destroys the referenced Entity
    public final static List<String> LIFECYCLE_TYPES = Arrays.asList(REL_CREATES, REL_UPDATES, REL_REMOVES);
    public final static List<String> EVENT_RELATIONS =
            Arrays.asList(REL_EVENT_RELATIONS, REL_ACTOR, REL_AFFECTS, REL_INVOLVES, REL_OBSERVES,
                    REL_REFERENCES, REL_CREATES, REL_UPDATES, REL_REMOVES, REL_TRANSACTION,
                    REL_SOLD_BY, REL_GRANTS_ACCES_TO, REL_BOUGHT_FOR, REL_SERIAL, REL_SUB_EVENT);

    public final static List<String> ENTITY_RELATIONS =
            Arrays.asList(REL_ENTITY_RELATIONS, REL_IS, REL_INTEGRAL_TO, REL_CLOSE, REL_KNOWS,
                    REL_KNOWS_OF, REL_AKA, REL_MEASURES, REL_SOCIAL, REL_ALIAS, REL_PART_OF,
                    REL_PAIRED_WITH, REL_POWERED_BY, REL_BELONGS_TO, REL_PROXY_FOR, REL_ON_BEHALF_OF, REL_RELAYED_BY,
                    REL_ASSOCIATED_WITH, REL_LOCATED_AT, REL_MEMBER_OF, REL_LOCATED_IN, REL_RELATED_TO,
                    REL_RATED_BY, REL_HAS_RELATIONS_TO, REL_OF_TYPE);

    public final static List<String> ALL_ENTITY_RELATIONS =
            Arrays.asList(REL_ENTITY_RELATIONS, REL_IS, REL_INTEGRAL_TO, REL_CLOSE, REL_KNOWS,
                    REL_KNOWS_OF, REL_AKA, REL_SOCIAL, REL_MEMBER_OF, REL_ALIAS, REL_PART_OF,
                    REL_PAIRED_WITH, REL_POWERED_BY, REL_BELONGS_TO, REL_PROXY_FOR, REL_ON_BEHALF_OF, REL_RELAYED_BY,
                    REL_ASSOCIATED_WITH, REL_LOCATED_AT, REL_MEASURES, REL_LOCATED_IN, REL_RELATED_TO,
                    REL_HAS_RELATIONS_TO, REL_OF_TYPE, REL_RATED_BY, REL_SUPPLIED_BY, REL_MANUFACTURED_BY,
                    REL_HOSTED_AT);

    /* Aspect Relations */
    public static final String REL_ASPECT = "ASPECT";             //Event has the aspect-entity as an aspect
    public static final String REL_CLASSIFIED = "CLASSIFIED";
    public static final String REL_TAGGED = "TAGGED";
    public static final String REL_SUBTYPE = "SUBTYPE";
    public static final String REL_LOCALIZED = "LOCALIZED";
    public static final String REL_EXPERIENCE_INDEX = "EXPERIENCE";
    public static final String REL_GROUPED_WITH = "GROUPED_WITH";
    public static final String REL_AB_RESULTS = "RESULTS";
    public static final String REL_ATTACHED = "ATTACHED";
    public static final String REL_RESIDES_AT = "RESIDES_AT";         //Address location
    public static final String REL_TRACKING_ASPECT = "TRACKING_ASPECT";    //Event has the aspect-entity as an aspect
    public static final String REL_CONNECTS_FROM = "CONNECTS_FROM";
    public static final String REL_USES_DEVICE = "USES_DEVICE";
    public static final String REL_MEASURED = "MEASURED";
    public static final String REL_LOCATION = "LOCATION";           //Geo location
    public static final String REL_TEMP_LOCATION = "TEMP_LOCATION";      //Temporary Geo location
    public static final String REL_TS = "TS_REL";             //Links Events or Entities to time series
    public static final String REL_VIEWED = "VIEWED";
    public static final String REL_REFERRED = "REFERRED";
    public final static List<String> ASPECT_RELATIONS =
            Arrays.asList(REL_TRACKING_ASPECT, REL_TEMP_LOCATION, REL_CONNECTS_FROM, REL_USES_DEVICE,
                    REL_MEASURED, REL_TS, REL_VIEWED, REL_REFERRED, REL_ATTACHED, REL_RESIDES_AT);

    // Embedded aspect document types
    // The names are kept short because OrientDB stores them with every record.
    public static final String EM_ADDRESS = "ASA";
    public static final String EM_CLASSIFICATION = "ASC";
    public static final String EM_CONTENT = "AS3";
    public static final String EM_DEMOGRAPHY = "ASD";
    public static final String EM_DIMENSIONS = "AS0";
    public static final String EM_GEO_LOCATION = "ASG";
    public static final String EM_INVENTORY = "ASI";
    public static final String EM_MESSAGING = "AS2";
    public static final String EM_LOCALE = "ASL";
    public static final String EM_METRICS = "ASM";

    public static final String REL_CORRESPONDENCE = "CORRESPONDENCE";
    public static final String REL_NOTIFICATION = "NOTIFICATION";
    public static final String REL_MESSAGE = "MESSAGE";
    public static final String REL_SENT_BY = "SENT_BY";
    public static final String REL_SENT_TO = "SENT_TO";
    public static final String REL_SENT_TO_CC = "SENT_TO_CC";
    public static final String REL_SENT_TO_BCC = "SENT_TO_BCC";
    public final static List<String> CORRESPONDENCE_TYPES =
            Arrays.asList(REL_CORRESPONDENCE, REL_NOTIFICATION, REL_MESSAGE, REL_SENT_BY, REL_SENT_TO,REL_SENT_TO_CC, REL_SENT_TO_BCC);

    /* Aspect Entities
     * Aspect entities are used to store Activity Stream Modelled Classes with schema/model
     * Mostly populated via aspects and through enrichment
     */
    public static final String AS_ASPECT_ENTITY = "ASAspectEntity";
    public static final String AS_ASPECT_VALUES = "ASAspectValues";

    public static final String AS_SETTING = "ASSetting";
    public static final String AS_TAG = "ASTag";
    public static final String AS_CATEGORY = "ASCategory";
    public static final String AS_CATEGORY_VALUE = "ASCategoryValue";
    public static final String AS_LOCALE = "ASLocale";
    public static final String AS_EXPERIENCE_INDEX = "ASExperienceIndex";

    public static final String AS_COLLAPSE_GROUP = "ASCollapseGroup";
    public static final String AS_AB_TEST = "ASABTest";
    public static final String AS_AB_TEST_OUTCOME = "ASABTestOutcome";
    public static final String AS_PRESENTATION = "ASPresentation";
    public static final String AS_SUMMARY = "ASSummary";

    public static final String AS_TIME_SERIES = "ASTimeSeries";
    public static final String AS_ATTACHMENT = "ASAttachment";
    public static final String AS_BLOCKABLE_ASPECT = "ASBlockable";

    //Internet Entities
    public static final String AS_IP_ADDRESS = "ASIP";
    public static final String AS_ISP = "ASISP";
    public static final String AS_CLIENT_DEVICE = "ASClientDevice";

    //Geographical Entities
    public static final String AS_CONTINENT = "ASContinent";
    public static final String AS_COUNTRY = "ASCountry";
    public static final String AS_CITY = "ASCity";
    public static final String AS_REGION = "ASRegion";
    public static final String AS_AREA = "ASArea";
    public static final String AS_DMA = "ASDMA";
    public static final String AS_POSTAL_CODE = "ASPostalCode";
    public static final String AS_STREET = "ASStreet";
    public static final String AS_ADDRESS = "ASAddress";
    public static final String AS_USER = "ASUser";
    public static final String AS_GEO_LOCATION = "ASGeoLocation";

    //Marketing Related Entities
    public static final String AS_TRAFFIC_SOURCE = "ASTrafficSource";
    public static final String AS_UTM_CAMPAIGN = "ASUtmCampaign";
    public static final String AS_UTM_SOURCE = "ASUtmSource";
    public static final String AS_UTM_MEDIUM = "ASUtmMedium";

    //Web Related Entities
    public static final String AS_WEB_PAGE = "ASWebPage";
    public static final String AS_WEB_DOMAIN = "ASWebDomain";
    public static final String AS_WEB_BROWSER = "ASWebBrowser";
    public static final String AS_WEB_SESSION = "ASWebSession";

    /* interest Relations */
    public static final String REL_INTEREST = "INTEREST";           //General interest

    /* social interest Relations */
    public static final String REL_SOCIAL_INTEREST = "SOCIAL_INTEREST";    //General interest

    public static final String REL_FOLLOWS = "FOLLOWS";            //A User follows entities (Abstract/Base)
    public static final String REL_MONITORS = "MONITORS";           //a Channel monitors entities (FOLLOWS)
    public static final String REL_SUBSCRIBES_TO = "SUBSCRIBES_TO";      //A User subscribes to a Channel (FOLLOWS)
    public static final String REL_MANAGES = "MANAGES";            //Manages (FOLLOWS)

    public static final String REL_MANAGED_BY = "MANAGED_BY";         //Manages (FOLLOWS)
    public static final String REL_BUMP = "BUMP";               //Base Bump Relation
    public static final String REL_BUMP_UP = "BUMP_UP";            //+1  / Like / Bump
    public static final String REL_BUMP_DOWN = "BUMP_DOWN";          //-1  / Dislike
    public static final String REL_BUMP_NEUTRAL = "BUMP_NEUTRAL";       //+-0 / I was here
    public static final String REL_COMMENT = "COMMENT";            //Base Comment Relations
    public static final String REL_COMMENTS = "COMMENTS";           //The actor entity that made the comment
    public static final String REL_COMMENTED_ON = "COMMENTED_ON";       //The entity that the comment belongs to
    public static final String REL_MENTIONS = "MENTIONS";           //The entity mentioned in the comment

    public static final String REL_VIEWED_BY = "VIEWED_BY";          //Viewed by entity

    public final static List<String> INTEREST_TYPES =
            Arrays.asList(REL_INTEREST, REL_FOLLOWS, REL_MANAGED_BY, REL_MANAGES, REL_BUMP,
                    REL_COMMENT, REL_COMMENTS, REL_COMMENTED_ON, REL_MENTIONS);


    /**
     * Purchase Relations
     **/
    public static final String REL_TRADE = "TRADE"; //Abstract Type

    //All mean that the customer got something (He is now in the possession off the entity)
    public static final String REL_PURCHASED = "PURCHASED";          //Something is purchased
    public static final String REL_REPLACED = "REPLACED";           //Something is replaced
    public static final String REL_REPURCHASED = "REPURCHASED";       //Something is re-purchased (sold by previous purchaser)
    public static final String REL_RESOLD = "RESOLD";             //Something is re-purchased (sold by previous purchaser) //Remove this! (use re-purchased
    public static final String REL_ORDERED = "ORDERED";          //Something is ordered
    public static final String REL_CANCELLED = "CANCELLED";          //Something is cancelled
    public static final String REL_RENTED = "RENTED";             //Something is rented
    public static final String REL_LEASED = "LEASED";             //Something is leased
    public static final String REL_GOT = "GOT";                //Something is "gotten" without being rented, leased or purchased
    public static final String REL_WON = "WON";                //Something is "won" without being rented, leased or purchased

    //All mean that the customer returned somethin (He is no longer in possession of)
    public static final String REL_RETURNED = "RETURNED";           //Something is returned (purchased, rented, leased, got or won)
    //public static final String REL_LOST                   = "LOST";

    //These things indicate what items are being selected or collected for purchasing (Customer is not yeat in the possession off but is interested in)
    //A wishlist is a type of cart
    public static final String REL_CARTED = "CARTED";             //Something is put in the cart
    public static final String REL_UN_CARTED = "UN_CARTED";          //Something is removed from the cart

    public static final String REL_RESERVED = "RESERVED";           //Something is reserved
    public static final String REL_UN_RESERVED = "UN_RESERVED";        //Something is un-reserved (cancelled)

    public static final String REL_UNAVAILABLE = "UNAVAILABLE";
    //Something product was not available when the customer wanted it //Means that the customer could not get the linked item

    public final static List<String> TRADE_TYPES =
            Arrays.asList(REL_TRADE, REL_PURCHASED, REL_ORDERED, REL_CANCELLED, REL_REPLACED, REL_REPURCHASED,
                    REL_RESOLD, REL_RENTED, REL_LEASED, REL_GOT, REL_RETURNED, REL_WON,
                    REL_RESERVED, REL_UN_RESERVED, REL_WON, REL_RESERVED, REL_UNAVAILABLE,
                    REL_CARTED, REL_UN_CARTED);
    public final static List<String> INVENTORY_TRADE_TYPES = Arrays.asList(REL_PURCHASED, REL_RETURNED);

    //Utility lookup lists
    public final static List<String> ACTOR_RELATIONS = Arrays.asList(REL_ACTOR, REL_BUYER);
    public final static List<String> TARGET_RELATIONS = Arrays.asList(REL_AFFECTS, REL_VIEWED, REL_SENT_TO);
    public final static List<String> PRODUCT_RELATIONS =
            Arrays.asList(REL_TRADE, REL_VIEWED, REL_RETURNED, REL_RESOLD, REL_PURCHASED,
                    REL_ORDERED, REL_CANCELLED, REL_REPLACED, REL_REPURCHASED, REL_GOT, REL_CARTED,
                    REL_UN_CARTED);

    /* Metadata Relations */
    public static final String REL_TRACKED_BY = "TRACKED_BY";         //An entity is tracked by this timeseries
    public static final String REL_MEASURED_BY = "MEASURED_BY";        //An entity is tracked by this timeseries
    public static final String REL_LOCATED_BY = "LOCATED_BY";         //An entity is tracked by this timeseries

    //todo - figure out if this is a sensible approach or if all classes should have biderectional values
    public static final Map<String, String> REVERSED_RELATIONS = new HashMap() {{
        put(REL_MANAGED_BY, REL_MANAGES);
        put(REL_COMMENTS, REL_COMMENTS);
        put(REL_SUPPLIED_BY, REL_SUPPLIED_BY);
    }};

    public final static List<String> COMMENT_RELATIONS = Arrays.asList(REL_COMMENTS, REL_COMMENTED_ON, REL_MENTIONS);


    /* Enrichments */
    public static final String IP_LOOKUP = "IPLookup";
    public static final String ZIPCODE_LOOKUP = "ZipCodeLookup";
    public static final String POSTCODE_LOOKUP = "PostcodeLookup";
    public static final String COUNTRY_LOOKUP = "CountryLookup";
    public static final String LOCATION_LOOKUP = "LocationLookup";
    public static final String NAMED_ENTITIES_LOOKUP = "NamedEntitiesLookup";
    public static final String CITY_LOOKUP = "CityLookup";
    public static final String GEO_COORDINATE_LOOKUP = "GeoCoordinateLookup";
    public static final String GEO_ADDRESS_LOOKUP = "GeoAddressLookup";
    public static final String DNS_LOOKUP = "DNSLookup";
    public static final String SOCIAL_LOOKUP = "SocialLookup";
    public static final String USER_AGENT_LOOKUP = "UserAgentLookup";
    public static final String GENDER_LOOKUP = "GenderLookup";
    public static final String PHONE_LOOKUP = "PhoneLookup";
    public static final String EMAIL_LOOKUP = "EmailLookup";
    public static final String NLP_SENTIMENT_LOOKUP = "NlpSentimentLookup";
    public static final String WEATHER_HOURLY_FORECAST_LOOKUP = "WeatherHourlyForecastLookup";
    public static final String WEATHER_DAILY_FORECAST_LOOKUP = "WeatherDailyForecastLookup";
    public static final String CURRENT_WEATHER_LOOKUP = "CurrentWeatherLookup";
    public static final String NLP_LANG_DETECT_LOOKUP = "NlpLanguageDetectionLookup";
    public static final String HOLIDAY_LOOKUP = "HolidayLookup";

    public static final List<String> ALL_ENTITY_ENRICHMENTS = Arrays.asList(IP_LOOKUP, USER_AGENT_LOOKUP, GENDER_LOOKUP, ZIPCODE_LOOKUP);
    public static final List<String> DEFAULT_ENTITY_ENRICHMENTS = Arrays.asList(IP_LOOKUP, USER_AGENT_LOOKUP);

}

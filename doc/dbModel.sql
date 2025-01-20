-- as user postgres first one time
CREATE ROLE fisher_admin WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION
  PASSWORD '1234567';

COMMENT ON ROLE fisher_admin IS 'fisher_admin';

CREATE DATABASE fisher
    WITH
    OWNER = fisher_admin
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;




-- as fisher_admin:
CREATE TABLE public."User" (
    "ID" bigint NOT NULL,
    "loginName" character varying(50) NOT NULL,
    "loginPassword" character varying(100) NOT NULL,
    "firstName" character varying(70) NOT NULL,
    "lastName" character varying(70) NOT NULL,
    email character varying(70) NOT NULL,
    "additionalInfo" character varying(255),
    corporation character varying(100),
    department character varying(50),
    city character varying(50),
    country character varying(50),
    "jobTitle" character varying(50),
    phone character varying(50),
    address character varying(100),
    "postalCode" character varying(50),
    "insertedBy" character varying(120),
    "modifiedBy" character varying(120),
    "deletedBy" character varying(120),
    "insertedAt" timestamp with time zone,
    "modifiedAt" timestamp with time zone,
    "deletedAt" timestamp with time zone,
    "trxId" bigint,
    "userTypeId" bigint
);


ALTER TABLE public."User" OWNER TO fisher_admin;

--
-- Name: User_ID_seq; Type: SEQUENCE; Schema: public; Owner: fisher_admin
--

CREATE SEQUENCE public."User_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."User_ID_seq" OWNER TO fisher_admin;

--
-- Name: User_ID_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fisher_admin
--

ALTER SEQUENCE public."User_ID_seq" OWNED BY public."User"."ID";

CREATE TABLE public.bilet (
    id integer NOT NULL,
    klient_id bigint NOT NULL,
    lowisko_id bigint NOT NULL,
    poczatek_waznosci date,
    koniec_waznosci date
);


ALTER TABLE public.bilet OWNER TO fisher_admin;

--
-- Name: bilet_id_seq; Type: SEQUENCE; Schema: public; Owner: fisher_admin
--

CREATE SEQUENCE public.bilet_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.bilet_id_seq OWNER TO fisher_admin;

--
-- Name: bilet_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fisher_admin
--

ALTER SEQUENCE public.bilet_id_seq OWNED BY public.bilet.id;


--
-- Name: cfg_role; Type: TABLE; Schema: public; Owner: fisher_admin
--

CREATE TABLE public.cfg_role (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    description character varying(125) NOT NULL,
    enabled boolean DEFAULT true,
    created timestamp with time zone DEFAULT now(),
    modified timestamp with time zone DEFAULT now(),
    "parentId" bigint NOT NULL,
    "insertedBy" character varying(120),
    "modifiedBy" character varying(120),
    "deletedBy" character varying(120),
    "insertedAt" timestamp with time zone,
    "modifiedAt" timestamp with time zone,
    "deletedAt" timestamp with time zone,
    "trxId" bigint
);


ALTER TABLE public.cfg_role OWNER TO fisher_admin;

--
-- Name: cfg_role_ID_seq; Type: SEQUENCE; Schema: public; Owner: fisher_admin
--

CREATE SEQUENCE public."cfg_role_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."cfg_role_ID_seq" OWNER TO fisher_admin;

--
-- Name: cfg_role_ID_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fisher_admin
--

ALTER SEQUENCE public."cfg_role_ID_seq" OWNED BY public.cfg_role.id;


--
-- Name: cfg_user; Type: TABLE; Schema: public; Owner: fisher_admin
--

CREATE TABLE public.cfg_user (
    id bigint NOT NULL,
    name character varying(254) NOT NULL,
    email character varying(254) NOT NULL,
    pswd character varying(254) NOT NULL,
    description character varying(254),
    enabled boolean DEFAULT true,
    "changePasswordNextLogin" boolean DEFAULT false,
    "lastLogin" timestamp with time zone DEFAULT now(),
    created timestamp with time zone DEFAULT now(),
    modified timestamp with time zone DEFAULT now(),
    "userId" bigint
);


ALTER TABLE public.cfg_user OWNER TO fisher_admin;

--
-- Name: cfg_user_ID_seq; Type: SEQUENCE; Schema: public; Owner: fisher_admin
--

CREATE SEQUENCE public."cfg_user_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."cfg_user_ID_seq" OWNER TO fisher_admin;

--
-- Name: cfg_user_ID_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fisher_admin
--

ALTER SEQUENCE public."cfg_user_ID_seq" OWNED BY public.cfg_user.id;

INSERT INTO public."cfg_user" (name, email, pswd)
values ('Kierownik', 'Kierownik', crypt('user', gen_salt('bf')));

--
-- Name: cfg_user_role_map; Type: TABLE; Schema: public; Owner: fisher_admin
--

CREATE TABLE public.cfg_user_role_map (
    "ID" bigint NOT NULL,
    cfg_user_id bigint,
    cfg_role_id bigint,
    enabled boolean DEFAULT true,
    created timestamp with time zone DEFAULT now(),
    modified timestamp with time zone DEFAULT now()
);


ALTER TABLE public.cfg_user_role_map OWNER TO fisher_admin;

--
-- Name: cfg_user_role_map_ID_seq; Type: SEQUENCE; Schema: public; Owner: fisher_admin
--

CREATE SEQUENCE public."cfg_user_role_map_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."cfg_user_role_map_ID_seq" OWNER TO fisher_admin;

--
-- Name: cfg_user_role_map_ID_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fisher_admin
--

ALTER SEQUENCE public."cfg_user_role_map_ID_seq" OWNED BY public.cfg_user_role_map."ID";


--
-- Name: cfg_user_role_map_view; Type: VIEW; Schema: public; Owner: fisher_admin
--

CREATE VIEW public.cfg_user_role_map_view AS
 SELECT cfg_user_role_map."ID",
    cfg_user.email,
    cfg_role.name
   FROM ((public.cfg_user_role_map
     LEFT JOIN public.cfg_user ON ((cfg_user.id = cfg_user_role_map.cfg_user_id)))
     LEFT JOIN public.cfg_role ON ((cfg_role.id = cfg_user_role_map.cfg_role_id)));


ALTER VIEW public.cfg_user_role_map_view OWNER TO fisher_admin;

--
-- Name: klient; Type: TABLE; Schema: public; Owner: fisher_admin
--

CREATE TABLE public.klient (
    id integer NOT NULL,
    email character varying(255),
    imie character varying(255),
    nazwisko character varying(255)
);


ALTER TABLE public.klient OWNER TO fisher_admin;

--
-- Name: klient_id_seq; Type: SEQUENCE; Schema: public; Owner: fisher_admin
--

CREATE SEQUENCE public.klient_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.klient_id_seq OWNER TO fisher_admin;

--
-- Name: klient_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fisher_admin
--

ALTER SEQUENCE public.klient_id_seq OWNED BY public.klient.id;


--
-- Name: lowisko; Type: TABLE; Schema: public; Owner: fisher_admin
--

CREATE TABLE public.lowisko (
    id integer NOT NULL,
    adres character varying(255)
);


ALTER TABLE public.lowisko OWNER TO fisher_admin;

--
-- Name: lowisko_id_seq; Type: SEQUENCE; Schema: public; Owner: fisher_admin
--

CREATE SEQUENCE public.lowisko_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.lowisko_id_seq OWNER TO fisher_admin;

--
-- Name: lowisko_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fisher_admin
--

ALTER SEQUENCE public.lowisko_id_seq OWNED BY public.lowisko.id;


--
-- Name: pracownik; Type: TABLE; Schema: public; Owner: fisher_admin
--

CREATE TABLE public.pracownik (
    id integer NOT NULL,
    imie character varying(255),
    nazwisko character varying(255),
    pesel bigint,
    telefon character varying(15),
    haslo_hash character varying(40)
);


ALTER TABLE public.pracownik OWNER TO fisher_admin;

--
-- Name: pracownik_id_seq; Type: SEQUENCE; Schema: public; Owner: fisher_admin
--

CREATE SEQUENCE public.pracownik_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pracownik_id_seq OWNER TO fisher_admin;

--
-- Name: pracownik_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fisher_admin
--

ALTER SEQUENCE public.pracownik_id_seq OWNED BY public.pracownik.id;


--
-- Name: pracownik_rola; Type: TABLE; Schema: public; Owner: fisher_admin
--

CREATE TABLE public.pracownik_rola (
    id integer NOT NULL,
    pracownik_id bigint NOT NULL,
    cfg_role_id bigint NOT NULL,
    data_zatrudnienia date,
    data_zwolnienia date
);


ALTER TABLE public.pracownik_rola OWNER TO fisher_admin;

--
-- Name: pracownik_rola_id_seq; Type: SEQUENCE; Schema: public; Owner: fisher_admin
--

CREATE SEQUENCE public.pracownik_rola_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pracownik_rola_id_seq OWNER TO fisher_admin;

--
-- Name: pracownik_rola_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fisher_admin
--

ALTER SEQUENCE public.pracownik_rola_id_seq OWNED BY public.pracownik_rola.id;


--
-- Name: rezerwacja; Type: TABLE; Schema: public; Owner: fisher_admin
--

CREATE TABLE public.rezerwacja (
    id integer NOT NULL,
    klient_id bigint NOT NULL,
    sprzet_id bigint NOT NULL,
    towar_id bigint NOT NULL
);


ALTER TABLE public.rezerwacja OWNER TO fisher_admin;

--
-- Name: rezerwacja_id_seq; Type: SEQUENCE; Schema: public; Owner: fisher_admin
--

CREATE SEQUENCE public.rezerwacja_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.rezerwacja_id_seq OWNER TO fisher_admin;

--
-- Name: rezerwacja_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fisher_admin
--

ALTER SEQUENCE public.rezerwacja_id_seq OWNED BY public.rezerwacja.id;


--
-- Name: serwis; Type: TABLE; Schema: public; Owner: fisher_admin
--

CREATE TABLE public.serwis (
    id integer NOT NULL,
    pracownik_id bigint NOT NULL,
    sprzet_id bigint NOT NULL,
    opis character varying(255)
);


ALTER TABLE public.serwis OWNER TO fisher_admin;

--
-- Name: serwis_id_seq; Type: SEQUENCE; Schema: public; Owner: fisher_admin
--

CREATE SEQUENCE public.serwis_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.serwis_id_seq OWNER TO fisher_admin;

--
-- Name: serwis_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fisher_admin
--

ALTER SEQUENCE public.serwis_id_seq OWNED BY public.serwis.id;


--
-- Name: sklep; Type: TABLE; Schema: public; Owner: fisher_admin
--

CREATE TABLE public.sklep (
    id integer NOT NULL,
    adres character varying(255)
);


ALTER TABLE public.sklep OWNER TO fisher_admin;

--
-- Name: sklep_id_seq; Type: SEQUENCE; Schema: public; Owner: fisher_admin
--

CREATE SEQUENCE public.sklep_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sklep_id_seq OWNER TO fisher_admin;

--
-- Name: sklep_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fisher_admin
--

ALTER SEQUENCE public.sklep_id_seq OWNED BY public.sklep.id;


--
-- Name: sprzedaz; Type: TABLE; Schema: public; Owner: fisher_admin
--

CREATE TABLE public.sprzedaz (
    id integer NOT NULL,
    pracownik_id bigint NOT NULL,
    transakcja_id character varying(255) NOT NULL,
    kwota_sprzedazy money
);


ALTER TABLE public.sprzedaz OWNER TO fisher_admin;

--
-- Name: sprzedaz_id_seq; Type: SEQUENCE; Schema: public; Owner: fisher_admin
--

CREATE SEQUENCE public.sprzedaz_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sprzedaz_id_seq OWNER TO fisher_admin;

--
-- Name: sprzedaz_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fisher_admin
--

ALTER SEQUENCE public.sprzedaz_id_seq OWNED BY public.sprzedaz.id;


--
-- Name: sprzet; Type: TABLE; Schema: public; Owner: fisher_admin
--

CREATE TABLE public.sprzet (
    id integer NOT NULL,
    sklep_id bigint NOT NULL,
    nazwa text NOT NULL,
    typ_sprzetu bigint NOT NULL,
    cena float NOT NULL
);


ALTER TABLE public.sprzet OWNER TO fisher_admin;

--
-- Name: sprzet_id_seq; Type: SEQUENCE; Schema: public; Owner: fisher_admin
--

CREATE SEQUENCE public.sprzet_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sprzet_id_seq OWNER TO fisher_admin;

--
-- Name: sprzet_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fisher_admin
--

ALTER SEQUENCE public.sprzet_id_seq OWNED BY public.sprzet.id;


--
-- Name: sprzet_wyporzyczenie; Type: TABLE; Schema: public; Owner: fisher_admin
--

CREATE TABLE public.sprzet_wyporzyczenie (
    id integer NOT NULL,
    sprzet_id bigint NOT NULL,
    wyporzyczenie_id bigint NOT NULL
);


ALTER TABLE public.sprzet_wyporzyczenie OWNER TO fisher_admin;

--
-- Name: sprzet_wyporzyczenie_id_seq; Type: SEQUENCE; Schema: public; Owner: fisher_admin
--

CREATE SEQUENCE public.sprzet_wyporzyczenie_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sprzet_wyporzyczenie_id_seq OWNER TO fisher_admin;

--
-- Name: sprzet_wyporzyczenie_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fisher_admin
--

ALTER SEQUENCE public.sprzet_wyporzyczenie_id_seq OWNED BY public.sprzet_wyporzyczenie.id;


--
-- Name: towar; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.towar (
    id integer NOT NULL,
    sklep_id bigint NOT NULL,
    nazwa text NOT NULL,
    typ_towaru bigint NOT NULL,
    ilosc bigint NOT NULL,
    cena float NOT NULL
);


ALTER TABLE public.towar OWNER TO fisher_admin;

--
-- Name: towar_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.towar_id_seq

    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.towar_id_seq OWNER TO fisher_admin;

--
-- Name: towar_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.towar_id_seq OWNED BY public.towar.id;

CREATE TABLE public.typ_towaru (
                                   id integer NOT NULL,
                                   nazwa character varying(255)
);

-- Set the owner of the table
ALTER TABLE public.typ_towaru OWNER TO fisher_admin;

-- Create a sequence for the `id` column
CREATE SEQUENCE public.typ_towaru_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Assign the sequence to the `id` column
ALTER SEQUENCE public.typ_towaru_id_seq OWNER TO fisher_admin;

ALTER SEQUENCE public.typ_towaru_id_seq OWNED BY public.typ_towaru.id;

--
-- Name: typ_sprzetu; Type: TABLE; Schema: public; Owner: fisher_admin
--

CREATE TABLE public.typ_sprzetu (
    id integer NOT NULL,
    nazwa character varying(255)
);


ALTER TABLE public.typ_sprzetu OWNER TO fisher_admin;

--
-- Name: typ_sprzetu_id_seq; Type: SEQUENCE; Schema: public; Owner: fisher_admin
--

CREATE SEQUENCE public.typ_sprzetu_id_seq

    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.typ_sprzetu_id_seq OWNER TO fisher_admin;

--
-- Name: typ_sprzetu_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fisher_admin
--

ALTER SEQUENCE public.typ_sprzetu_id_seq OWNED BY public.typ_sprzetu.id;


--
-- Name: wyporzyczenie; Type: TABLE; Schema: public; Owner: fisher_admin
--

CREATE TABLE public.wyporzyczenie (
    id integer NOT NULL,
    pracownik_id bigint NOT NULL,
    klient_id bigint NOT NULL,
    sprzet_id bigint NOT NULL,
    data_zwrotu date
);


ALTER TABLE public.wyporzyczenie OWNER TO fisher_admin;

--
-- Name: wyporzyczenie_id_seq; Type: SEQUENCE; Schema: public; Owner: fisher_admin
--

CREATE SEQUENCE public.wyporzyczenie_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.wyporzyczenie_id_seq OWNER TO fisher_admin;

--
-- Name: wyporzyczenie_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fisher_admin
--

ALTER SEQUENCE public.wyporzyczenie_id_seq OWNED BY public.wyporzyczenie.id;
--
-- Name: bilet id; Type: DEFAULT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.bilet ALTER COLUMN id SET DEFAULT nextval('public.bilet_id_seq'::regclass);



--
-- Name: klient id; Type: DEFAULT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.klient ALTER COLUMN id SET DEFAULT nextval('public.klient_id_seq'::regclass);


--
-- Name: lowisko id; Type: DEFAULT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.lowisko ALTER COLUMN id SET DEFAULT nextval('public.lowisko_id_seq'::regclass);


--
-- Name: pracownik id; Type: DEFAULT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.pracownik ALTER COLUMN id SET DEFAULT nextval('public.pracownik_id_seq'::regclass);


--
-- Name: pracownik_rola id; Type: DEFAULT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.pracownik_rola ALTER COLUMN id SET DEFAULT nextval('public.pracownik_rola_id_seq'::regclass);


--
-- Name: rezerwacja id; Type: DEFAULT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.rezerwacja ALTER COLUMN id SET DEFAULT nextval('public.rezerwacja_id_seq'::regclass);


--
-- Name: serwis id; Type: DEFAULT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.serwis ALTER COLUMN id SET DEFAULT nextval('public.serwis_id_seq'::regclass);


--
-- Name: sklep id; Type: DEFAULT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.sklep ALTER COLUMN id SET DEFAULT nextval('public.sklep_id_seq'::regclass);


--
-- Name: sprzedaz id; Type: DEFAULT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.sprzedaz ALTER COLUMN id SET DEFAULT nextval('public.sprzedaz_id_seq'::regclass);


--
-- Name: sprzet id; Type: DEFAULT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.sprzet ALTER COLUMN id SET DEFAULT nextval('public.sprzet_id_seq'::regclass);


--
-- Name: sprzet_wyporzyczenie id; Type: DEFAULT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.sprzet_wyporzyczenie ALTER COLUMN id SET DEFAULT nextval('public.sprzet_wyporzyczenie_id_seq'::regclass);


--
-- Name: towar id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.towar ALTER COLUMN id SET DEFAULT nextval('public.towar_id_seq'::regclass);


--
-- Name: typ_sprzetu id; Type: DEFAULT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.typ_sprzetu ALTER COLUMN id SET DEFAULT nextval('public.typ_sprzetu_id_seq'::regclass);


--
-- Name: wyporzyczenie id; Type: DEFAULT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.wyporzyczenie ALTER COLUMN id SET DEFAULT nextval('public.wyporzyczenie_id_seq'::regclass);



--
-- Name: User_ID_seq; Type: SEQUENCE SET; Schema: public; Owner: fisher_admin
--

SELECT pg_catalog.setval('public."User_ID_seq"', 1, false);


--
-- Name: bilet_id_seq; Type: SEQUENCE SET; Schema: public; Owner: fisher_admin
--

SELECT pg_catalog.setval('public.bilet_id_seq', 1, false);


--
-- Name: cfg_role_ID_seq; Type: SEQUENCE SET; Schema: public; Owner: fisher_admin
--

SELECT pg_catalog.setval('public."cfg_role_ID_seq"', 1, true);


--
-- Name: cfg_user_ID_seq; Type: SEQUENCE SET; Schema: public; Owner: fisher_admin
--

SELECT pg_catalog.setval('public."cfg_user_ID_seq"', 1, false);


--
-- Name: cfg_user_role_map_ID_seq; Type: SEQUENCE SET; Schema: public; Owner: fisher_admin
--

SELECT pg_catalog.setval('public."cfg_user_role_map_ID_seq"', 1, false);


--
-- Name: klient_id_seq; Type: SEQUENCE SET; Schema: public; Owner: fisher_admin
--

SELECT pg_catalog.setval('public.klient_id_seq', 1, false);


--
-- Name: lowisko_id_seq; Type: SEQUENCE SET; Schema: public; Owner: fisher_admin
--

SELECT pg_catalog.setval('public.lowisko_id_seq', 1, false);


--
-- Name: pracownik_id_seq; Type: SEQUENCE SET; Schema: public; Owner: fisher_admin
--

SELECT pg_catalog.setval('public.pracownik_id_seq', 1, false);


--
-- Name: pracownik_rola_id_seq; Type: SEQUENCE SET; Schema: public; Owner: fisher_admin
--

SELECT pg_catalog.setval('public.pracownik_rola_id_seq', 1, false);


--
-- Name: rezerwacja_id_seq; Type: SEQUENCE SET; Schema: public; Owner: fisher_admin
--

SELECT pg_catalog.setval('public.rezerwacja_id_seq', 1, false);


--
-- Name: serwis_id_seq; Type: SEQUENCE SET; Schema: public; Owner: fisher_admin
--

SELECT pg_catalog.setval('public.serwis_id_seq', 1, false);


--
-- Name: sklep_id_seq; Type: SEQUENCE SET; Schema: public; Owner: fisher_admin
--

SELECT pg_catalog.setval('public.sklep_id_seq', 1, false);


--
-- Name: sprzedaz_id_seq; Type: SEQUENCE SET; Schema: public; Owner: fisher_admin
--

SELECT pg_catalog.setval('public.sprzedaz_id_seq', 1, false);


--
-- Name: sprzet_id_seq; Type: SEQUENCE SET; Schema: public; Owner: fisher_admin
--

SELECT pg_catalog.setval('public.sprzet_id_seq', 1, false);


--
-- Name: sprzet_wyporzyczenie_id_seq; Type: SEQUENCE SET; Schema: public; Owner: fisher_admin
--

SELECT pg_catalog.setval('public.sprzet_wyporzyczenie_id_seq', 1, false);


--
-- Name: towar_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.towar_id_seq', 1, false);


--
-- Name: typ_sprzetu_id_seq; Type: SEQUENCE SET; Schema: public; Owner: fisher_admin
--

SELECT pg_catalog.setval('public.typ_sprzetu_id_seq', 1, false);


--
-- Name: wyporzyczenie_id_seq; Type: SEQUENCE SET; Schema: public; Owner: fisher_admin
--

SELECT pg_catalog.setval('public.wyporzyczenie_id_seq', 1, false);


--
-- Name: bilet bilet_pkey; Type: CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.bilet
    ADD CONSTRAINT bilet_pkey PRIMARY KEY (id);


--
-- Name: cfg_role cfg_role_pkey; Type: CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.cfg_role
    ADD CONSTRAINT cfg_role_pkey PRIMARY KEY (id);


--
-- Name: klient klient_pkey; Type: CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.klient
    ADD CONSTRAINT klient_pkey PRIMARY KEY (id);


--
-- Name: lowisko lowisko_pkey; Type: CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.lowisko
    ADD CONSTRAINT lowisko_pkey PRIMARY KEY (id);


--
-- Name: pracownik pracownik_pkey; Type: CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.pracownik
    ADD CONSTRAINT pracownik_pkey PRIMARY KEY (id);


--
-- Name: pracownik_rola pracownik_rola_pkey; Type: CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.pracownik_rola
    ADD CONSTRAINT pracownik_rola_pkey PRIMARY KEY (id);


--
-- Name: rezerwacja rezerwacja_pkey; Type: CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.rezerwacja
    ADD CONSTRAINT rezerwacja_pkey PRIMARY KEY (id);


--
-- Name: serwis serwis_pkey; Type: CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.serwis
    ADD CONSTRAINT serwis_pkey PRIMARY KEY (id);


--
-- Name: sklep sklep_pkey; Type: CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.sklep
    ADD CONSTRAINT sklep_pkey PRIMARY KEY (id);


--
-- Name: sprzedaz sprzedaz_pkey; Type: CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.sprzedaz
    ADD CONSTRAINT sprzedaz_pkey PRIMARY KEY (id);


--
-- Name: sprzet sprzet_pkey; Type: CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.sprzet
    ADD CONSTRAINT sprzet_pkey PRIMARY KEY (id);


--
-- Name: sprzet_wyporzyczenie sprzet_wyporzyczenie_pkey; Type: CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.sprzet_wyporzyczenie
    ADD CONSTRAINT sprzet_wyporzyczenie_pkey PRIMARY KEY (id);


--
-- Name: towar towar_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.towar
    ADD CONSTRAINT towar_pkey PRIMARY KEY (id);


--
-- Name: typ_sprzetu typ_sprzetu_pkey; Type: CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.typ_sprzetu
    ADD CONSTRAINT typ_sprzetu_pkey PRIMARY KEY (id);


--
-- Name: wyporzyczenie wyporzyczenie_pkey; Type: CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.wyporzyczenie
    ADD CONSTRAINT wyporzyczenie_pkey PRIMARY KEY (id);


--
-- Name: bilet fk_bilet_klient; Type: FK CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.bilet
    ADD CONSTRAINT fk_bilet_klient FOREIGN KEY (klient_id) REFERENCES public.klient(id);


--
-- Name: bilet fk_bilet_lowisko; Type: FK CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.bilet
    ADD CONSTRAINT fk_bilet_lowisko FOREIGN KEY (lowisko_id) REFERENCES public.lowisko(id);


--
-- Name: wyporzyczenie fk_klient; Type: FK CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.wyporzyczenie
    ADD CONSTRAINT fk_klient FOREIGN KEY (klient_id) REFERENCES public.klient(id);


--
-- Name: pracownik_rola fk_pracownik; Type: FK CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.pracownik_rola
    ADD CONSTRAINT fk_pracownik FOREIGN KEY (pracownik_id) REFERENCES public.pracownik(id);


--
-- Name: wyporzyczenie fk_pracownik; Type: FK CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.wyporzyczenie
    ADD CONSTRAINT fk_pracownik FOREIGN KEY (pracownik_id) REFERENCES public.pracownik(id);


--
-- Name: rezerwacja fk_rezerwacja_klient; Type: FK CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.rezerwacja
    ADD CONSTRAINT fk_rezerwacja_klient FOREIGN KEY (klient_id) REFERENCES public.klient(id);


--
-- Name: rezerwacja fk_rezerwacja_sprzet; Type: FK CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.rezerwacja
    ADD CONSTRAINT fk_rezerwacja_sprzet FOREIGN KEY (sprzet_id) REFERENCES public.sprzet(id);


--
-- Name: rezerwacja fk_rezerwacja_towar; Type: FK CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.rezerwacja
    ADD CONSTRAINT fk_rezerwacja_towar FOREIGN KEY (towar_id) REFERENCES public.towar(id);


--
-- Name: pracownik_rola fk_role; Type: FK CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.pracownik_rola
    ADD CONSTRAINT fk_role FOREIGN KEY (cfg_role_id) REFERENCES public.cfg_role(id);


--
-- Name: serwis fk_serwis_pracownik; Type: FK CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.serwis
    ADD CONSTRAINT fk_serwis_pracownik FOREIGN KEY (pracownik_id) REFERENCES public.pracownik(id);


--
-- Name: serwis fk_serwis_sprzet; Type: FK CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.serwis
    ADD CONSTRAINT fk_serwis_sprzet FOREIGN KEY (sprzet_id) REFERENCES public.sprzet(id);


--
-- Name: towar fk_sklep; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.towar
    ADD CONSTRAINT fk_sklep FOREIGN KEY (sklep_id) REFERENCES public.sklep(id);


--
-- Name: sprzet fk_sklep; Type: FK CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.sprzet
    ADD CONSTRAINT fk_sklep FOREIGN KEY (sklep_id) REFERENCES public.sklep(id);


--
-- Name: sprzedaz fk_sprzedaz_pracownik; Type: FK CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.sprzedaz
    ADD CONSTRAINT fk_sprzedaz_pracownik FOREIGN KEY (pracownik_id) REFERENCES public.pracownik(id);


--
-- Name: wyporzyczenie fk_sprzet; Type: FK CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.wyporzyczenie
    ADD CONSTRAINT fk_sprzet FOREIGN KEY (sprzet_id) REFERENCES public.sprzet(id);


--
-- Name: sprzet_wyporzyczenie fk_sprzet_id; Type: FK CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.sprzet_wyporzyczenie
    ADD CONSTRAINT fk_sprzet_id FOREIGN KEY (sprzet_id) REFERENCES public.sprzet(id);


--
-- Name: sprzet fk_typ_sprzetu; Type: FK CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.sprzet
    ADD CONSTRAINT fk_typ_sprzetu FOREIGN KEY (typ_sprzetu_id) REFERENCES public.typ_sprzetu(id);


--
-- Name: sprzet_wyporzyczenie fk_wyporzyczenie_id; Type: FK CONSTRAINT; Schema: public; Owner: fisher_admin
--

ALTER TABLE ONLY public.sprzet_wyporzyczenie
    ADD CONSTRAINT fk_wyporzyczenie_id FOREIGN KEY (wyporzyczenie_id) REFERENCES public.wyporzyczenie(id);



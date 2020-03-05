# jPlatformSEO

<p>
  <a href="https://travis-ci.org/organizations/departement-loire-atlantique">
    <img src="https://travis-ci.org/departement-loire-atlantique/jPlatformSEO.svg?branch=master" />
  </a>
  <!--
  <a href="https://sonarcloud.io/organizations/departement-loire-atlantique">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=departement-loire-atlantique_jPlatformSEO&metric=ncloc" />
    <img src="https://sonarcloud.io/api/project_badges/measure?project=departement-loire-atlantique_jPlatformSEO&metric=bugs" />
    <img src="https://sonarcloud.io/api/project_badges/measure?project=departement-loire-atlantique_jPlatformSEO&metric=code_smells" />
    <img src="https://sonarcloud.io/api/project_badges/measure?project=departement-loire-atlantique_jPlatformSEO&metric=coverage" />
    <img src="https://sonarcloud.io/api/project_badges/measure?project=departement-loire-atlantique_jPlatformSEO&metric=duplicated_lines_density" />
    <img src="https://sonarcloud.io/api/project_badges/measure?project=departement-loire-atlantique_jPlatformSEO&metric=sqale_rating" />
    <img src="https://sonarcloud.io/api/project_badges/measure?project=departement-loire-atlantique_jPlatformSEO&metric=alert_status" />
    <img src="https://sonarcloud.io/api/project_badges/measure?project=departement-loire-atlantique_jPlatformSEO&metric=reliability_rating" />
    <img src="https://sonarcloud.io/api/project_badges/measure?project=departement-loire-atlantique_jPlatformSEO&metric=security_rating" />
    <img src="https://sonarcloud.io/api/project_badges/measure?project=departement-loire-atlantique_jPlatformSEO&metric=sqale_index" />
    <img src="https://sonarcloud.io/api/project_badges/measure?project=departement-loire-atlantique_jPlatformSEO&metric=vulnerabilities" />
    </a>
    -->
</p>

Ce module améliore les fonctionnalités SEO pour JPlatform10.

Liste des fonctionnalités en bref :

* Plan du site personnalisable
    * Pour les internautes
    * Pour les robots
* Fichier robots.txt personnalisé
* Optimisation / réécriture des URLS
* Outil d'aide à la contribution SEO
* Balises META pour les réseaux sociaux
* Pages d'erreur 404 et 403 personnalisées
* Page site en maintenance
* Personnalisation du libellé "jcms" pour l'URL de JCMS
* Personnalisation du titre de la page ?
* Marqueur Google Tag Manager avec variables personnalisées

## Plan du site personnalisable

### Plan du site pour les internaute

Fichier JSP plugins/SEOPlugin/jsp/sitemap.jsp

Réalise automatiquement un plan du site basé sur une liste de catégories en ignorant certaines catégories (via extradata sur les caégories).

Configuration :

* La propriété *fr.cg44.plugin.seo.sitemap.cats* permet de lister les catégories racines (Liste d'ID de catégories)

MAJ : on utilisera finalement une porlet Navigation avancée qui permettra de saisir les différentes catégories racine.
Cette portlet permettra aussi de paramétrer le nombre de niveaux.
* La propriété *fr.cg44.plugin.seo.sitemap.stopcats* permet de lister les catégories filles à ignorer (Liste d'ID de catégories)
--> FAUX : il s'agit d'extradata.


### Plan du site pour les robots

Fichier JSP sitemap.jsp à mettre à la racine du site.

Génération automatique d'un fichier sitemap.xml à déclarer dans google search console pour le site.

Le module sitemap respectera les droits sur les catégories et les contenus (Module CategoryRight et natif JCMS).

Configuration :

* La propriété *fr.cg44.plugin.seo.sitemapxml.type.blacklist* permet d'ignorer des types de contenu (Liste de chaine de caractères)
* La propriété *fr.cg44.plugin.seo.sitemapxml.mime.blacklist* permet d'ignorer certains documents relativement à leur types MIMES

**Demande DCom : pouvoir visualiser facilement le paramétrage fait (blacklist,..), d'une manière + conviviale que via les propriétés de modules.**


## Fichier robots.txt personnalisé

La configuration de ce fichier permet de concentrer l'indexation des moteurs de recherche sur les contenus HTML du site.

Voir fichier : /robots.txt

**ATTENTION : besoin d'avoir des robots.txt différents en fonction des sous-domaines.**


## Fil d'ariane personnalisé et configurable

Le fil d'ariane se base sur les catégories du contenu actuellement affiché sur le site.  
Une propriété permet actuellement de préciser la branche de catégorie utilisée pour la navigation.  
Je propose de supprimer cette propriété et de mettre la catégorie de navigation directement dnas la portlet navigation. C'est son rôle et son fonctionnement.

Il faut maintenir le champ "Texte au sein du fil d'Ariane" pour éventuellement renommer des catégories trop longues.

Le fait de masquer une catégorie dans le fil d'ariane n'est pas utile.
    
    
Un gabarit de **Portlet navigation** permet d'effectuer le rendu :

```
plugins/SEOPlugin/types/PortletNavigate/breadcrumb.jsp
```


## Meta "Robots"

2 états possibles : soit on indexe la page et on suit les liens, soit on n'indexe pas la page et on ne suit pas les liens.

Le meta robot aura alors soit les valeurs "index,follow", soit "noindex, nofollow".

Les différentes pages du sites seront soit une catégorie, soit une publication.  
La règle est la suivante : tant que la publication/catégorie/portail n'est pas dans un état visible pour un lecteur anonyme, alors la page ne doit pas être indexée ("noindex, nofollow").  

L'idée est de ne pas indexéer les rubriques et contenus en cours de construction.

L'état est défini par les droits JCMS et le module CategoryRights.

Syntaxe de la balise : 

```
<meta name="robots" content="index,follow" />
```

## Rel canonical (à compléter)

Donner également la possibilité de renseigner une balise "rel canonical" sur chaque page de type éviterait également la duplication.
Le but est d'éviter d'avoir des URLs différentes pour un même contenu. C'est pénalisant pour l'indexation.
 
```
<link rel="canonical" href="http://www.monsite.com/url-canonique.html"/>
```

**Demande DCom : fournir un export des contenus multicatégorisés (branche "Services")**

Questions : si un contenu est catégorisé X fois, quelle URL générer ? Possibilité de la générer automatiquement... **A creuser !!!**



## Optimisation / réécriture des URLS

Afin d'optimiser le référencement des contenus, le titre de la page dans l'URL ainsi que la méta donnée **description** seront configurables.

Cette fonctionnalité repose sur deux extra pour toutes les catégories, les portlets et les publications :

* Titre (URL page et META)
* Description (META)

**La meta "keywords" est à supprimer**

Note : l'extra data actuellement dédiée aux catégories ("Affichée oui/non dans l'URL") n'est plus utile puisqu'on se limite à l'affichage de la catégorie "parente".


 
Une personnalisation automatique des propriétés JCMS relativement aux URLs des pages : descriptive-urls.*

Format URL contenu : /departement44/première_catégorie_autorisée_trouvée/titre/id

Format URL membre : /departement44/première_catégorie_autorisée_trouvée/titre/id

Format URL category : /departement44/première_catégorie_autorisée_trouvée/titre/id

Format URL autre : /departement44/première_catégorie_autorisée_trouvée/titre/id

**IMPORTANT : si un contenu est rattaché à plusieurs thématiques, la première catégorie autorisée trouvée est aléatoire et dépend d'un algorithme propre à jPlatform.**


Un PortalPolicyFilter permettra :

* de calculer la syntaxe de l'URL à afficher.
* d'ajouter automatiquement dans la page les META titre, description, et robots
* d'ajouter automatiquement dans la page la META last-modified


## Outil d'aide à la contribution SEO

Tableau de bord en back office permettant de voir les valeurs des propriétés clés du module ainsi que la liste des catégories pour lesquelles une personnalisation extradata a été configurée.
       
## Balises META pour les réseaux sociaux (à compléter avec la DCom - Gaëlle + Clémentine)

- A préciser : qu'est ce qui dynamique, qu'est qui est lié à une propriété - 

Exemple :

```
<meta property="og:site_name" content="Loire-atlantique.fr" />
<meta property="og:url" content="https://www.loire-atlantique.fr/jcms/services-fr-c_5026" />
<meta property="og:type" content="website" />
<meta property="og:title" content="Informations pratiques et services en ligne" />
<meta property="og:description" content="loire-atlantique.fr : services, contacts, aides et informations pratiques ciblés pour l&apos;enfance, la famille, la jeunesse, les personnes âgées, les personnes en situation de handicap, l&apos;éducation, le RSA, l&apos;insertion, l&apos;emploi, l&apos;économie, l&apos;environnement, la culture, le sport, le logement, le tourisme, les aides aux territoires, les subventions départementales.
Retrouvez aussi toutes les informations pratiques pour votre quotidien  : infotrafic, horaires des transports LILA , agenda des loisirs et des événements incontournables en Loire-Atlantique. " />
<meta property="og:image" content="https://www.loire-atlantique.fr/upload/docs/image/png/2015-04/depla_site_favicon_196x196.png" />
<meta name="twitter:card" content="summary" />
<meta name="twitter:site" content="loireatlantique" />
```
 
## Pages d'erreur 404 et 403 personnalisées

Deux gabarits de pages statiques et un PortalPolicyFilter permettent d'afficher des pages personnalisés en cas d'URL introuvable dans JCMS. Lors de l'accès à une ressource protégée, une page personnaliser apparait permettant de revenir à l'accueil ou de s'authentifier (code d'erreur 403 pour éviter l'indexation par Google).

Ces pages sont également configurée dans apache (conf à préciser) afin de fonctionner aussi pour des URLs non prises en charges dans JCMS.

Exemple (hors jcms) : https://www.loire-atlantique.fr/nexitepas
Exemple (jcms) : https://www.loire-atlantique.fr/jcms/nexitepas


## Marqueur Google Tag Manager (GTM) avec variables personnalisées

Si la propriété *fr.cg44.plugin.seo.analytics* est renseignée avec le code du marqueur analytics du site, alors le marqueur GTM est automatiquement intégré dans toutes les pages du site.

Code à insérer :
 
```
<!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=UA-46270573-1"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'UA-46270573-1');
</script>
```

Si la propriété *fr.cg44.plugin.seo.google-site-verification* est renseignée, alors la META suivante est automatiquement intégré dans toutes les pages du site :

```
<meta name="google-site-verification" content="fNMrbG81fIcmTXv-vSbSPmaJYs3Jt1cux6YSx7uNoUQ" />
```

Listes des variables personnalisées :

- ariane catégorie : id de la catégorie / id de la catégorie / ...
- ariane nommée : nom de la catégorie / nom de la catégorie / ...
- espace de travail : nom

Configuration :

* La propriété *fr.cg44.plugin.seo.analyticscats* permet de choisir les branches de navigation pour le calcul des niveaux des variables personnalisées (la première catégorie qui correspond à une catégorie du contenu est utilisée)


## Installation de ce module

Attention : conséquence sur les URL, penser à mettre en place un plan de redirection des URLs
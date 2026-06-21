-- ============================================================
-- ROLLBACK de la migration question_id / choice_id
-- Généré le 2026-06-18T13:38:15.196Z
-- Script source : mig_all_question_choice_ids.sql
-- ============================================================
--
-- ⚠️  ORDRE D'EXÉCUTION CRITIQUE :
--   1. D'abord rollback ÉTAPE 2 (choice_id) car les WHERE utilisent
--      les nouveaux question_id (déjà en base après l'ÉTAPE 1).
--   2. Ensuite rollback ÉTAPE 1 (question_id) pour remettre les anciens.
--
-- Questions à annuler : 224 (224 consultation_results + 224 reponses_consultation)
-- Choix à annuler     : 1052 (1052 consultation_results + 1052 reponses_consultation)
-- ============================================================

-- ============================================================
-- ROLLBACK ÉTAPE 2 : Restauration des anciens choice_id
-- (utilise les nouveaux question_id encore présents en base)
-- ============================================================

-- Consultation kmbc0ie5zsx94dejoiv1wcnv / question 40054
UPDATE consultation_results SET choice_id = '13' WHERE choice_id = '196' AND question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '13' WHERE choice_id = '196' AND question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '14' WHERE choice_id = '221' AND question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '14' WHERE choice_id = '221' AND question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '15' WHERE choice_id = '235' AND question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '15' WHERE choice_id = '235' AND question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '16' WHERE choice_id = '245' AND question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '16' WHERE choice_id = '245' AND question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '12' WHERE choice_id = '171' AND question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '12' WHERE choice_id = '171' AND question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation dy9l5rzk9r3ocurra7xanvi7 / question 20190
UPDATE consultation_results SET choice_id = '31' WHERE choice_id = '1538' AND question_id = '20190' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '31' WHERE choice_id = '1538' AND question_id = '20190' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';

-- Consultation jf84upj9qutugwkce773m8dt / question 40056
UPDATE consultation_results SET choice_id = '32' WHERE choice_id = '173' AND question_id = '40056' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '32' WHERE choice_id = '173' AND question_id = '40056' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '33' WHERE choice_id = '198' AND question_id = '40056' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '33' WHERE choice_id = '198' AND question_id = '40056' AND consultation_id = 'jf84upj9qutugwkce773m8dt';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 40046
UPDATE consultation_results SET choice_id = '34' WHERE choice_id = '163' AND question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '34' WHERE choice_id = '163' AND question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '35' WHERE choice_id = '188' AND question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '35' WHERE choice_id = '188' AND question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '36' WHERE choice_id = '213' AND question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '36' WHERE choice_id = '213' AND question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '37' WHERE choice_id = '228' AND question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '37' WHERE choice_id = '228' AND question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '55' WHERE choice_id = '241' AND question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '55' WHERE choice_id = '241' AND question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 40050
UPDATE consultation_results SET choice_id = '47' WHERE choice_id = '167' AND question_id = '40050' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '47' WHERE choice_id = '167' AND question_id = '40050' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '48' WHERE choice_id = '217' AND question_id = '40050' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '48' WHERE choice_id = '217' AND question_id = '40050' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '45' WHERE choice_id = '192' AND question_id = '40050' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '45' WHERE choice_id = '192' AND question_id = '40050' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 40049
UPDATE consultation_results SET choice_id = '49' WHERE choice_id = '166' AND question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '49' WHERE choice_id = '166' AND question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '42' WHERE choice_id = '216' AND question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '42' WHERE choice_id = '216' AND question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '43' WHERE choice_id = '191' AND question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '43' WHERE choice_id = '191' AND question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '44' WHERE choice_id = '231' AND question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '44' WHERE choice_id = '231' AND question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '46' WHERE choice_id = '243' AND question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '46' WHERE choice_id = '243' AND question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 40048
UPDATE consultation_results SET choice_id = '50' WHERE choice_id = '165' AND question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '50' WHERE choice_id = '165' AND question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '52' WHERE choice_id = '215' AND question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '52' WHERE choice_id = '215' AND question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '53' WHERE choice_id = '230' AND question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '53' WHERE choice_id = '230' AND question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '54' WHERE choice_id = '242' AND question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '54' WHERE choice_id = '242' AND question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '51' WHERE choice_id = '190' AND question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '51' WHERE choice_id = '190' AND question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 40066
UPDATE consultation_results SET choice_id = '93' WHERE choice_id = '183' AND question_id = '40066' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '93' WHERE choice_id = '183' AND question_id = '40066' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '94' WHERE choice_id = '208' AND question_id = '40066' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '94' WHERE choice_id = '208' AND question_id = '40066' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 40067
UPDATE consultation_results SET choice_id = '95' WHERE choice_id = '184' AND question_id = '40067' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '95' WHERE choice_id = '184' AND question_id = '40067' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '96' WHERE choice_id = '209' AND question_id = '40067' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '96' WHERE choice_id = '209' AND question_id = '40067' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '97' WHERE choice_id = '225' AND question_id = '40067' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '97' WHERE choice_id = '225' AND question_id = '40067' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '99' WHERE choice_id = '238' AND question_id = '40067' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '99' WHERE choice_id = '238' AND question_id = '40067' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 40070
UPDATE consultation_results SET choice_id = '98' WHERE choice_id = '187' AND question_id = '40070' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '98' WHERE choice_id = '187' AND question_id = '40070' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '100' WHERE choice_id = '212' AND question_id = '40070' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '100' WHERE choice_id = '212' AND question_id = '40070' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 40061
UPDATE consultation_results SET choice_id = '83' WHERE choice_id = '250' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '83' WHERE choice_id = '250' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '84' WHERE choice_id = '251' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '84' WHERE choice_id = '251' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '78' WHERE choice_id = '178' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '78' WHERE choice_id = '178' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '79' WHERE choice_id = '203' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '79' WHERE choice_id = '203' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '80' WHERE choice_id = '224' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '80' WHERE choice_id = '224' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '81' WHERE choice_id = '237' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '81' WHERE choice_id = '237' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '82' WHERE choice_id = '247' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '82' WHERE choice_id = '247' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 40063
UPDATE consultation_results SET choice_id = '87' WHERE choice_id = '180' AND question_id = '40063' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '87' WHERE choice_id = '180' AND question_id = '40063' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '88' WHERE choice_id = '205' AND question_id = '40063' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '88' WHERE choice_id = '205' AND question_id = '40063' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 40064
UPDATE consultation_results SET choice_id = '89' WHERE choice_id = '181' AND question_id = '40064' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '89' WHERE choice_id = '181' AND question_id = '40064' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '90' WHERE choice_id = '206' AND question_id = '40064' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '90' WHERE choice_id = '206' AND question_id = '40064' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 40065
UPDATE consultation_results SET choice_id = '91' WHERE choice_id = '182' AND question_id = '40065' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '91' WHERE choice_id = '182' AND question_id = '40065' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '92' WHERE choice_id = '207' AND question_id = '40065' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '92' WHERE choice_id = '207' AND question_id = '40065' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation pwr61j5qhpm6ekrf3liuw5bc / question 20146
UPDATE consultation_results SET choice_id = '101' WHERE choice_id = '1851' AND question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '101' WHERE choice_id = '1851' AND question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '102' WHERE choice_id = '1652' AND question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '102' WHERE choice_id = '1652' AND question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '103' WHERE choice_id = '2019' AND question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '103' WHERE choice_id = '2019' AND question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '104' WHERE choice_id = '2158' AND question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '104' WHERE choice_id = '2158' AND question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '105' WHERE choice_id = '2263' AND question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '105' WHERE choice_id = '2263' AND question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';

-- Consultation pwr61j5qhpm6ekrf3liuw5bc / question 20147
UPDATE consultation_results SET choice_id = '106' WHERE choice_id = '1653' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '106' WHERE choice_id = '1653' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '107' WHERE choice_id = '1454' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '107' WHERE choice_id = '1454' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '108' WHERE choice_id = '1852' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '108' WHERE choice_id = '1852' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '109' WHERE choice_id = '2020' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '109' WHERE choice_id = '2020' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '110' WHERE choice_id = '2159' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '110' WHERE choice_id = '2159' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '111' WHERE choice_id = '2264' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '111' WHERE choice_id = '2264' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';

-- Consultation esg67bdrkzh0n29j2c858ryj / question 40057
UPDATE consultation_results SET choice_id = '125' WHERE choice_id = '222' AND question_id = '40057' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '125' WHERE choice_id = '222' AND question_id = '40057' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '126' WHERE choice_id = '199' AND question_id = '40057' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '126' WHERE choice_id = '199' AND question_id = '40057' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '124' WHERE choice_id = '174' AND question_id = '40057' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '124' WHERE choice_id = '174' AND question_id = '40057' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 20148
UPDATE consultation_results SET choice_id = '127' WHERE choice_id = '2023' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '127' WHERE choice_id = '2023' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '128' WHERE choice_id = '2267' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '128' WHERE choice_id = '2267' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '129' WHERE choice_id = '2340' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '129' WHERE choice_id = '2340' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '130' WHERE choice_id = '2394' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '130' WHERE choice_id = '2394' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '131' WHERE choice_id = '1855' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '131' WHERE choice_id = '1855' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '132' WHERE choice_id = '2423' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '132' WHERE choice_id = '2423' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '133' WHERE choice_id = '2162' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '133' WHERE choice_id = '2162' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '134' WHERE choice_id = '2441' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '134' WHERE choice_id = '2441' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '135' WHERE choice_id = '2451' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '135' WHERE choice_id = '2451' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '136' WHERE choice_id = '2456' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '136' WHERE choice_id = '2456' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '137' WHERE choice_id = '2461' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '137' WHERE choice_id = '2461' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 20149
UPDATE consultation_results SET choice_id = '142' WHERE choice_id = '1459' AND question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '142' WHERE choice_id = '1459' AND question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '143' WHERE choice_id = '2025' AND question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '143' WHERE choice_id = '2025' AND question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '144' WHERE choice_id = '1857' AND question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '144' WHERE choice_id = '1857' AND question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '145' WHERE choice_id = '1658' AND question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '145' WHERE choice_id = '1658' AND question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '146' WHERE choice_id = '2163' AND question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '146' WHERE choice_id = '2163' AND question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 20152
UPDATE consultation_results SET choice_id = '163' WHERE choice_id = '1661' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '163' WHERE choice_id = '1661' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '165' WHERE choice_id = '2270' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '165' WHERE choice_id = '2270' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '166' WHERE choice_id = '2343' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '166' WHERE choice_id = '2343' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '167' WHERE choice_id = '1860' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '167' WHERE choice_id = '1860' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '168' WHERE choice_id = '2166' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '168' WHERE choice_id = '2166' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '169' WHERE choice_id = '2028' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '169' WHERE choice_id = '2028' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '170' WHERE choice_id = '1462' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '170' WHERE choice_id = '1462' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 40069
UPDATE consultation_results SET choice_id = '156' WHERE choice_id = '227' AND question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '156' WHERE choice_id = '227' AND question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '157' WHERE choice_id = '186' AND question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '157' WHERE choice_id = '186' AND question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '158' WHERE choice_id = '211' AND question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '158' WHERE choice_id = '211' AND question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '154' WHERE choice_id = '240' AND question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '154' WHERE choice_id = '240' AND question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '155' WHERE choice_id = '249' AND question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '155' WHERE choice_id = '249' AND question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 40053
UPDATE consultation_results SET choice_id = '159' WHERE choice_id = '234' AND question_id = '40053' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '159' WHERE choice_id = '234' AND question_id = '40053' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '160' WHERE choice_id = '170' AND question_id = '40053' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '160' WHERE choice_id = '170' AND question_id = '40053' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '161' WHERE choice_id = '195' AND question_id = '40053' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '161' WHERE choice_id = '195' AND question_id = '40053' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '162' WHERE choice_id = '220' AND question_id = '40053' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '162' WHERE choice_id = '220' AND question_id = '40053' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 20151
UPDATE consultation_results SET choice_id = '164' WHERE choice_id = '2396' AND question_id = '20151' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '164' WHERE choice_id = '2396' AND question_id = '20151' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 20150
UPDATE consultation_results SET choice_id = '147' WHERE choice_id = '2164' AND question_id = '20150' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '147' WHERE choice_id = '2164' AND question_id = '20150' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '148' WHERE choice_id = '1858' AND question_id = '20150' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '148' WHERE choice_id = '1858' AND question_id = '20150' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 40068
UPDATE consultation_results SET choice_id = '149' WHERE choice_id = '185' AND question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '149' WHERE choice_id = '185' AND question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '150' WHERE choice_id = '239' AND question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '150' WHERE choice_id = '239' AND question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '151' WHERE choice_id = '210' AND question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '151' WHERE choice_id = '210' AND question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '152' WHERE choice_id = '248' AND question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '152' WHERE choice_id = '248' AND question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '153' WHERE choice_id = '226' AND question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '153' WHERE choice_id = '226' AND question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 20153
UPDATE consultation_results SET choice_id = '177' WHERE choice_id = '1465' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '177' WHERE choice_id = '1465' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '178' WHERE choice_id = '1664' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '178' WHERE choice_id = '1664' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '179' WHERE choice_id = '1863' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '179' WHERE choice_id = '1863' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '180' WHERE choice_id = '2029' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '180' WHERE choice_id = '2029' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '181' WHERE choice_id = '2167' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '181' WHERE choice_id = '2167' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '183' WHERE choice_id = '2271' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '183' WHERE choice_id = '2271' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 20158
UPDATE consultation_results SET choice_id = '185' WHERE choice_id = '1672' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '185' WHERE choice_id = '1672' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '186' WHERE choice_id = '1870' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '186' WHERE choice_id = '1870' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '187' WHERE choice_id = '2036' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '187' WHERE choice_id = '2036' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '188' WHERE choice_id = '2173' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '188' WHERE choice_id = '2173' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '189' WHERE choice_id = '2276' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '189' WHERE choice_id = '2276' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '190' WHERE choice_id = '2348' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '190' WHERE choice_id = '2348' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '191' WHERE choice_id = '2401' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '191' WHERE choice_id = '2401' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '192' WHERE choice_id = '2428' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '192' WHERE choice_id = '2428' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '833' WHERE choice_id = '1473' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '833' WHERE choice_id = '1473' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 20160
UPDATE consultation_results SET choice_id = '193' WHERE choice_id = '1475' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '193' WHERE choice_id = '1475' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '194' WHERE choice_id = '1674' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '194' WHERE choice_id = '1674' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '195' WHERE choice_id = '2278' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '195' WHERE choice_id = '2278' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '196' WHERE choice_id = '2038' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '196' WHERE choice_id = '2038' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '197' WHERE choice_id = '1872' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '197' WHERE choice_id = '1872' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '198' WHERE choice_id = '2175' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '198' WHERE choice_id = '2175' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '199' WHERE choice_id = '2350' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '199' WHERE choice_id = '2350' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 20161
UPDATE consultation_results SET choice_id = '204' WHERE choice_id = '1676' AND question_id = '20161' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '204' WHERE choice_id = '1676' AND question_id = '20161' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '205' WHERE choice_id = '1874' AND question_id = '20161' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '205' WHERE choice_id = '1874' AND question_id = '20161' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '207' WHERE choice_id = '2040' AND question_id = '20161' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '207' WHERE choice_id = '2040' AND question_id = '20161' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '830' WHERE choice_id = '1477' AND question_id = '20161' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '830' WHERE choice_id = '1477' AND question_id = '20161' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 20162
UPDATE consultation_results SET choice_id = '206' WHERE choice_id = '1677' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '206' WHERE choice_id = '1677' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '208' WHERE choice_id = '1875' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '208' WHERE choice_id = '1875' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '209' WHERE choice_id = '2041' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '209' WHERE choice_id = '2041' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '210' WHERE choice_id = '2176' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '210' WHERE choice_id = '2176' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '211' WHERE choice_id = '2279' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '211' WHERE choice_id = '2279' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '212' WHERE choice_id = '2351' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '212' WHERE choice_id = '2351' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '213' WHERE choice_id = '2402' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '213' WHERE choice_id = '2402' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '831' WHERE choice_id = '1478' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '831' WHERE choice_id = '1478' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 20172
UPDATE consultation_results SET choice_id = '229' WHERE choice_id = '1495' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '229' WHERE choice_id = '1495' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '230' WHERE choice_id = '2055' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '230' WHERE choice_id = '2055' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '231' WHERE choice_id = '1694' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '231' WHERE choice_id = '1694' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '232' WHERE choice_id = '1891' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '232' WHERE choice_id = '1891' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '233' WHERE choice_id = '2188' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '233' WHERE choice_id = '2188' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '234' WHERE choice_id = '2360' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '234' WHERE choice_id = '2360' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '235' WHERE choice_id = '2290' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '235' WHERE choice_id = '2290' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 20163
UPDATE consultation_results SET choice_id = '220' WHERE choice_id = '1680' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '220' WHERE choice_id = '1680' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '221' WHERE choice_id = '1878' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '221' WHERE choice_id = '1878' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '222' WHERE choice_id = '2280' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '222' WHERE choice_id = '2280' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '223' WHERE choice_id = '2177' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '223' WHERE choice_id = '2177' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '224' WHERE choice_id = '2042' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '224' WHERE choice_id = '2042' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '225' WHERE choice_id = '2352' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '225' WHERE choice_id = '2352' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '226' WHERE choice_id = '2403' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '226' WHERE choice_id = '2403' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '832' WHERE choice_id = '1481' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '832' WHERE choice_id = '1481' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 20173
UPDATE consultation_results SET choice_id = '236' WHERE choice_id = '1892' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '236' WHERE choice_id = '1892' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '237' WHERE choice_id = '1695' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '237' WHERE choice_id = '1695' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '238' WHERE choice_id = '1496' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '238' WHERE choice_id = '1496' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '239' WHERE choice_id = '2291' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '239' WHERE choice_id = '2291' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '240' WHERE choice_id = '2189' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '240' WHERE choice_id = '2189' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '241' WHERE choice_id = '2056' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '241' WHERE choice_id = '2056' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 20133
UPDATE consultation_results SET choice_id = '252' WHERE choice_id = '1429' AND question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '252' WHERE choice_id = '1429' AND question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '253' WHERE choice_id = '1628' AND question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '253' WHERE choice_id = '1628' AND question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '254' WHERE choice_id = '1827' AND question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '254' WHERE choice_id = '1827' AND question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '255' WHERE choice_id = '2001' AND question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '255' WHERE choice_id = '2001' AND question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '256' WHERE choice_id = '2142' AND question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '256' WHERE choice_id = '2142' AND question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 20134
UPDATE consultation_results SET choice_id = '257' WHERE choice_id = '1629' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '257' WHERE choice_id = '1629' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '258' WHERE choice_id = '1828' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '258' WHERE choice_id = '1828' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '259' WHERE choice_id = '2002' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '259' WHERE choice_id = '2002' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '260' WHERE choice_id = '2143' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '260' WHERE choice_id = '2143' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '261' WHERE choice_id = '2251' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '261' WHERE choice_id = '2251' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '262' WHERE choice_id = '2329' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '262' WHERE choice_id = '2329' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '263' WHERE choice_id = '1430' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '263' WHERE choice_id = '1430' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 20135
UPDATE consultation_results SET choice_id = '274' WHERE choice_id = '1632' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '274' WHERE choice_id = '1632' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '275' WHERE choice_id = '1831' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '275' WHERE choice_id = '1831' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '276' WHERE choice_id = '2253' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '276' WHERE choice_id = '2253' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '277' WHERE choice_id = '2004' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '277' WHERE choice_id = '2004' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '278' WHERE choice_id = '2145' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '278' WHERE choice_id = '2145' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '279' WHERE choice_id = '2331' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '279' WHERE choice_id = '2331' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '280' WHERE choice_id = '2420' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '280' WHERE choice_id = '2420' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '281' WHERE choice_id = '2388' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '281' WHERE choice_id = '2388' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '282' WHERE choice_id = '1433' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '282' WHERE choice_id = '1433' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 20139
UPDATE consultation_results SET choice_id = '283' WHERE choice_id = '1439' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '283' WHERE choice_id = '1439' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '284' WHERE choice_id = '1837' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '284' WHERE choice_id = '1837' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '285' WHERE choice_id = '1638' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '285' WHERE choice_id = '1638' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '286' WHERE choice_id = '2009' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '286' WHERE choice_id = '2009' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '287' WHERE choice_id = '2150' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '287' WHERE choice_id = '2150' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '288' WHERE choice_id = '2333' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '288' WHERE choice_id = '2333' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '289' WHERE choice_id = '2255' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '289' WHERE choice_id = '2255' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '290' WHERE choice_id = '2390' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '290' WHERE choice_id = '2390' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '295' WHERE choice_id = '2421' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '295' WHERE choice_id = '2421' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 20140
UPDATE consultation_results SET choice_id = '291' WHERE choice_id = '1838' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '291' WHERE choice_id = '1838' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '292' WHERE choice_id = '2334' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '292' WHERE choice_id = '2334' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '293' WHERE choice_id = '2151' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '293' WHERE choice_id = '2151' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '294' WHERE choice_id = '2256' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '294' WHERE choice_id = '2256' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '296' WHERE choice_id = '2010' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '296' WHERE choice_id = '2010' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '297' WHERE choice_id = '2391' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '297' WHERE choice_id = '2391' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '298' WHERE choice_id = '2422' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '298' WHERE choice_id = '2422' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '299' WHERE choice_id = '2440' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '299' WHERE choice_id = '2440' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '300' WHERE choice_id = '1440' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '300' WHERE choice_id = '1440' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '301' WHERE choice_id = '1639' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '301' WHERE choice_id = '1639' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 20141
UPDATE consultation_results SET choice_id = '311' WHERE choice_id = '1444' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '311' WHERE choice_id = '1444' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '312' WHERE choice_id = '1643' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '312' WHERE choice_id = '1643' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '313' WHERE choice_id = '1842' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '313' WHERE choice_id = '1842' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '314' WHERE choice_id = '2011' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '314' WHERE choice_id = '2011' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '315' WHERE choice_id = '2152' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '315' WHERE choice_id = '2152' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '316' WHERE choice_id = '2335' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '316' WHERE choice_id = '2335' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '317' WHERE choice_id = '2257' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '317' WHERE choice_id = '2257' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '318' WHERE choice_id = '2392' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '318' WHERE choice_id = '2392' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 20142
UPDATE consultation_results SET choice_id = '319' WHERE choice_id = '2336' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '319' WHERE choice_id = '2336' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '320' WHERE choice_id = '1445' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '320' WHERE choice_id = '1445' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '321' WHERE choice_id = '1644' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '321' WHERE choice_id = '1644' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '322' WHERE choice_id = '2258' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '322' WHERE choice_id = '2258' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '323' WHERE choice_id = '1843' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '323' WHERE choice_id = '1843' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '324' WHERE choice_id = '2012' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '324' WHERE choice_id = '2012' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '325' WHERE choice_id = '2153' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '325' WHERE choice_id = '2153' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation kmbc0ie5zsx94dejoiv1wcnv / question 20165
UPDATE consultation_results SET choice_id = '348' WHERE choice_id = '1686' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '348' WHERE choice_id = '1686' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '349' WHERE choice_id = '1883' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '349' WHERE choice_id = '1883' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '351' WHERE choice_id = '2180' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '351' WHERE choice_id = '2180' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '352' WHERE choice_id = '2047' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '352' WHERE choice_id = '2047' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '353' WHERE choice_id = '1487' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '353' WHERE choice_id = '1487' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '354' WHERE choice_id = '2282' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '354' WHERE choice_id = '2282' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '355' WHERE choice_id = '2354' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '355' WHERE choice_id = '2354' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation kmbc0ie5zsx94dejoiv1wcnv / question 20166
UPDATE consultation_results SET choice_id = '356' WHERE choice_id = '1687' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '356' WHERE choice_id = '1687' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '357' WHERE choice_id = '1488' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '357' WHERE choice_id = '1488' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '358' WHERE choice_id = '1884' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '358' WHERE choice_id = '1884' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '359' WHERE choice_id = '2048' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '359' WHERE choice_id = '2048' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '360' WHERE choice_id = '2283' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '360' WHERE choice_id = '2283' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '361' WHERE choice_id = '2181' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '361' WHERE choice_id = '2181' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '363' WHERE choice_id = '2355' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '363' WHERE choice_id = '2355' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation kmbc0ie5zsx94dejoiv1wcnv / question 20167
UPDATE consultation_results SET choice_id = '362' WHERE choice_id = '1489' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '362' WHERE choice_id = '1489' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '364' WHERE choice_id = '1688' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '364' WHERE choice_id = '1688' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '365' WHERE choice_id = '1885' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '365' WHERE choice_id = '1885' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '366' WHERE choice_id = '2049' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '366' WHERE choice_id = '2049' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '367' WHERE choice_id = '2182' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '367' WHERE choice_id = '2182' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '368' WHERE choice_id = '2284' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '368' WHERE choice_id = '2284' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '369' WHERE choice_id = '2356' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '369' WHERE choice_id = '2356' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '370' WHERE choice_id = '2405' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '370' WHERE choice_id = '2405' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '371' WHERE choice_id = '2444' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '371' WHERE choice_id = '2444' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '372' WHERE choice_id = '2429' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '372' WHERE choice_id = '2429' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation kmbc0ie5zsx94dejoiv1wcnv / question 20169
UPDATE consultation_results SET choice_id = '380' WHERE choice_id = '1491' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '380' WHERE choice_id = '1491' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '381' WHERE choice_id = '1690' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '381' WHERE choice_id = '1690' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '382' WHERE choice_id = '2051' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '382' WHERE choice_id = '2051' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '383' WHERE choice_id = '1887' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '383' WHERE choice_id = '1887' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '384' WHERE choice_id = '2358' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '384' WHERE choice_id = '2358' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '385' WHERE choice_id = '2286' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '385' WHERE choice_id = '2286' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '386' WHERE choice_id = '2406' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '386' WHERE choice_id = '2406' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '388' WHERE choice_id = '2430' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '388' WHERE choice_id = '2430' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '390' WHERE choice_id = '2184' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '390' WHERE choice_id = '2184' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation kmbc0ie5zsx94dejoiv1wcnv / question 20168
UPDATE consultation_results SET choice_id = '373' WHERE choice_id = '1490' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '373' WHERE choice_id = '1490' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '374' WHERE choice_id = '1886' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '374' WHERE choice_id = '1886' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '375' WHERE choice_id = '2050' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '375' WHERE choice_id = '2050' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '376' WHERE choice_id = '1689' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '376' WHERE choice_id = '1689' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '377' WHERE choice_id = '2183' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '377' WHERE choice_id = '2183' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '378' WHERE choice_id = '2285' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '378' WHERE choice_id = '2285' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '379' WHERE choice_id = '2357' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '379' WHERE choice_id = '2357' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation w5d0rrhdlqby9shpt4wdk5oj / question 20154
UPDATE consultation_results SET choice_id = '395' WHERE choice_id = '1864' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '395' WHERE choice_id = '1864' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '396' WHERE choice_id = '2168' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '396' WHERE choice_id = '2168' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '397' WHERE choice_id = '2030' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '397' WHERE choice_id = '2030' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '398' WHERE choice_id = '2272' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '398' WHERE choice_id = '2272' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '399' WHERE choice_id = '2344' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '399' WHERE choice_id = '2344' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '400' WHERE choice_id = '2397' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '400' WHERE choice_id = '2397' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '401' WHERE choice_id = '1467' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '401' WHERE choice_id = '1467' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '402' WHERE choice_id = '1666' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '402' WHERE choice_id = '1666' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '403' WHERE choice_id = '2425' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '403' WHERE choice_id = '2425' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';

-- Consultation w5d0rrhdlqby9shpt4wdk5oj / question 20155
UPDATE consultation_results SET choice_id = '410' WHERE choice_id = '1470' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '410' WHERE choice_id = '1470' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '413' WHERE choice_id = '1669' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '413' WHERE choice_id = '1669' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '414' WHERE choice_id = '1867' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '414' WHERE choice_id = '1867' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '416' WHERE choice_id = '2033' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '416' WHERE choice_id = '2033' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '417' WHERE choice_id = '2170' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '417' WHERE choice_id = '2170' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '418' WHERE choice_id = '2273' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '418' WHERE choice_id = '2273' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '419' WHERE choice_id = '2345' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '419' WHERE choice_id = '2345' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '420' WHERE choice_id = '2398' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '420' WHERE choice_id = '2398' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';

-- Consultation w5d0rrhdlqby9shpt4wdk5oj / question 20157
UPDATE consultation_results SET choice_id = '433' WHERE choice_id = '2172' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '433' WHERE choice_id = '2172' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '434' WHERE choice_id = '2275' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '434' WHERE choice_id = '2275' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '435' WHERE choice_id = '2347' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '435' WHERE choice_id = '2347' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '436' WHERE choice_id = '2400' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '436' WHERE choice_id = '2400' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '437' WHERE choice_id = '2427' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '437' WHERE choice_id = '2427' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '438' WHERE choice_id = '1472' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '438' WHERE choice_id = '1472' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '439' WHERE choice_id = '2443' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '439' WHERE choice_id = '2443' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '440' WHERE choice_id = '1671' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '440' WHERE choice_id = '1671' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '441' WHERE choice_id = '2035' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '441' WHERE choice_id = '2035' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '442' WHERE choice_id = '1869' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '442' WHERE choice_id = '1869' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';

-- Consultation w5d0rrhdlqby9shpt4wdk5oj / question 20156
UPDATE consultation_results SET choice_id = '421' WHERE choice_id = '1471' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '421' WHERE choice_id = '1471' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '422' WHERE choice_id = '1670' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '422' WHERE choice_id = '1670' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '423' WHERE choice_id = '1868' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '423' WHERE choice_id = '1868' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '424' WHERE choice_id = '2034' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '424' WHERE choice_id = '2034' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '425' WHERE choice_id = '2171' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '425' WHERE choice_id = '2171' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '426' WHERE choice_id = '2346' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '426' WHERE choice_id = '2346' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '427' WHERE choice_id = '2274' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '427' WHERE choice_id = '2274' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '428' WHERE choice_id = '2399' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '428' WHERE choice_id = '2399' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '429' WHERE choice_id = '2426' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '429' WHERE choice_id = '2426' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '430' WHERE choice_id = '2442' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '430' WHERE choice_id = '2442' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '431' WHERE choice_id = '2452' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '431' WHERE choice_id = '2452' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '432' WHERE choice_id = '2457' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '432' WHERE choice_id = '2457' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 20196
UPDATE consultation_results SET choice_id = '484' WHERE choice_id = '1757' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '484' WHERE choice_id = '1757' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '485' WHERE choice_id = '1558' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '485' WHERE choice_id = '1558' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '486' WHERE choice_id = '1947' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '486' WHERE choice_id = '1947' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '487' WHERE choice_id = '2099' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '487' WHERE choice_id = '2099' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '488' WHERE choice_id = '2215' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '488' WHERE choice_id = '2215' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '489' WHERE choice_id = '2310' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '489' WHERE choice_id = '2310' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '490' WHERE choice_id = '2373' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '490' WHERE choice_id = '2373' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '491' WHERE choice_id = '2415' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '491' WHERE choice_id = '2415' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '492' WHERE choice_id = '2436' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '492' WHERE choice_id = '2436' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 20197
UPDATE consultation_results SET choice_id = '509' WHERE choice_id = '1565' AND question_id = '20197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '509' WHERE choice_id = '1565' AND question_id = '20197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '512' WHERE choice_id = '1764' AND question_id = '20197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '512' WHERE choice_id = '1764' AND question_id = '20197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '515' WHERE choice_id = '1951' AND question_id = '20197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '515' WHERE choice_id = '1951' AND question_id = '20197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '516' WHERE choice_id = '2102' AND question_id = '20197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '516' WHERE choice_id = '2102' AND question_id = '20197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 20209
UPDATE consultation_results SET choice_id = '539' WHERE choice_id = '1587' AND question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '539' WHERE choice_id = '1587' AND question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '541' WHERE choice_id = '1969' AND question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '541' WHERE choice_id = '1969' AND question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '542' WHERE choice_id = '2119' AND question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '542' WHERE choice_id = '2119' AND question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '543' WHERE choice_id = '1786' AND question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '543' WHERE choice_id = '1786' AND question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '544' WHERE choice_id = '2231' AND question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '544' WHERE choice_id = '2231' AND question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 20210
UPDATE consultation_results SET choice_id = '545' WHERE choice_id = '1588' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '545' WHERE choice_id = '1588' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '546' WHERE choice_id = '2120' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '546' WHERE choice_id = '2120' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '547' WHERE choice_id = '1787' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '547' WHERE choice_id = '1787' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '548' WHERE choice_id = '1970' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '548' WHERE choice_id = '1970' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '549' WHERE choice_id = '2232' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '549' WHERE choice_id = '2232' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '550' WHERE choice_id = '2320' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '550' WHERE choice_id = '2320' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '551' WHERE choice_id = '2382' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '551' WHERE choice_id = '2382' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 20211
UPDATE consultation_results SET choice_id = '571' WHERE choice_id = '1596' AND question_id = '20211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '571' WHERE choice_id = '1596' AND question_id = '20211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '572' WHERE choice_id = '1795' AND question_id = '20211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '572' WHERE choice_id = '1795' AND question_id = '20211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '573' WHERE choice_id = '1974' AND question_id = '20211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '573' WHERE choice_id = '1974' AND question_id = '20211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '574' WHERE choice_id = '2122' AND question_id = '20211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '574' WHERE choice_id = '2122' AND question_id = '20211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 20180
UPDATE consultation_results SET choice_id = '588' WHERE choice_id = '1514' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '588' WHERE choice_id = '1514' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '589' WHERE choice_id = '1907' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '589' WHERE choice_id = '1907' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '590' WHERE choice_id = '1713' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '590' WHERE choice_id = '1713' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '591' WHERE choice_id = '2067' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '591' WHERE choice_id = '2067' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '592' WHERE choice_id = '2196' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '592' WHERE choice_id = '2196' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '593' WHERE choice_id = '2295' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '593' WHERE choice_id = '2295' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 20181
UPDATE consultation_results SET choice_id = '603' WHERE choice_id = '1517' AND question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '603' WHERE choice_id = '1517' AND question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '604' WHERE choice_id = '1716' AND question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '604' WHERE choice_id = '1716' AND question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '605' WHERE choice_id = '2069' AND question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '605' WHERE choice_id = '2069' AND question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '606' WHERE choice_id = '1910' AND question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '606' WHERE choice_id = '1910' AND question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '608' WHERE choice_id = '2198' AND question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '608' WHERE choice_id = '2198' AND question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 20182
UPDATE consultation_results SET choice_id = '607' WHERE choice_id = '1518' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '607' WHERE choice_id = '1518' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '609' WHERE choice_id = '1717' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '609' WHERE choice_id = '1717' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '610' WHERE choice_id = '1911' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '610' WHERE choice_id = '1911' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '611' WHERE choice_id = '2070' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '611' WHERE choice_id = '2070' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '612' WHERE choice_id = '2297' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '612' WHERE choice_id = '2297' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '614' WHERE choice_id = '2199' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '614' WHERE choice_id = '2199' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 20183
UPDATE consultation_results SET choice_id = '623' WHERE choice_id = '1521' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '623' WHERE choice_id = '1521' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '624' WHERE choice_id = '1914' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '624' WHERE choice_id = '1914' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '625' WHERE choice_id = '1720' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '625' WHERE choice_id = '1720' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '626' WHERE choice_id = '2073' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '626' WHERE choice_id = '2073' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '627' WHERE choice_id = '2201' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '627' WHERE choice_id = '2201' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '628' WHERE choice_id = '2363' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '628' WHERE choice_id = '2363' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '630' WHERE choice_id = '2298' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '630' WHERE choice_id = '2298' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '631' WHERE choice_id = '2409' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '631' WHERE choice_id = '2409' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation vkp77x0ognt41xyjicz9wwba / question 20199
UPDATE consultation_results SET choice_id = '645' WHERE choice_id = '2108' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '645' WHERE choice_id = '2108' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '646' WHERE choice_id = '1958' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '646' WHERE choice_id = '1958' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '647' WHERE choice_id = '1575' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '647' WHERE choice_id = '1575' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '648' WHERE choice_id = '1774' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '648' WHERE choice_id = '1774' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '649' WHERE choice_id = '2311' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '649' WHERE choice_id = '2311' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '650' WHERE choice_id = '2221' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '650' WHERE choice_id = '2221' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '652' WHERE choice_id = '2374' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '652' WHERE choice_id = '2374' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';

-- Consultation vkp77x0ognt41xyjicz9wwba / question 20198
UPDATE consultation_results SET choice_id = '640' WHERE choice_id = '1574' AND question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '640' WHERE choice_id = '1574' AND question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '641' WHERE choice_id = '1773' AND question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '641' WHERE choice_id = '1773' AND question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '642' WHERE choice_id = '1957' AND question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '642' WHERE choice_id = '1957' AND question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '643' WHERE choice_id = '2107' AND question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '643' WHERE choice_id = '2107' AND question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '644' WHERE choice_id = '2220' AND question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '644' WHERE choice_id = '2220' AND question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';

-- Consultation vkp77x0ognt41xyjicz9wwba / question 20200
UPDATE consultation_results SET choice_id = '651' WHERE choice_id = '1576' AND question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '651' WHERE choice_id = '1576' AND question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '653' WHERE choice_id = '1775' AND question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '653' WHERE choice_id = '1775' AND question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '654' WHERE choice_id = '1959' AND question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '654' WHERE choice_id = '1959' AND question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '655' WHERE choice_id = '2109' AND question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '655' WHERE choice_id = '2109' AND question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '661' WHERE choice_id = '2222' AND question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '661' WHERE choice_id = '2222' AND question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';

-- Consultation vkp77x0ognt41xyjicz9wwba / question 20201
UPDATE consultation_results SET choice_id = '656' WHERE choice_id = '1577' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '656' WHERE choice_id = '1577' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '657' WHERE choice_id = '1776' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '657' WHERE choice_id = '1776' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '658' WHERE choice_id = '2110' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '658' WHERE choice_id = '2110' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '659' WHERE choice_id = '2223' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '659' WHERE choice_id = '2223' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '660' WHERE choice_id = '1960' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '660' WHERE choice_id = '1960' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '662' WHERE choice_id = '2312' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '662' WHERE choice_id = '2312' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '663' WHERE choice_id = '2375' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '663' WHERE choice_id = '2375' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '665' WHERE choice_id = '2416' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '665' WHERE choice_id = '2416' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';

-- Consultation rn3ib6tpe4yv3l7ofolyqh7d / question 20213
UPDATE consultation_results SET choice_id = '669' WHERE choice_id = '1601' AND question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '669' WHERE choice_id = '1601' AND question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '670' WHERE choice_id = '1979' AND question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '670' WHERE choice_id = '1979' AND question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '671' WHERE choice_id = '1800' AND question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '671' WHERE choice_id = '1800' AND question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '672' WHERE choice_id = '2125' AND question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '672' WHERE choice_id = '2125' AND question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '673' WHERE choice_id = '2236' AND question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '673' WHERE choice_id = '2236' AND question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';

-- Consultation rn3ib6tpe4yv3l7ofolyqh7d / question 20214
UPDATE consultation_results SET choice_id = '684' WHERE choice_id = '1804' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '684' WHERE choice_id = '1804' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '685' WHERE choice_id = '1983' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '685' WHERE choice_id = '1983' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '686' WHERE choice_id = '1605' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '686' WHERE choice_id = '1605' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '687' WHERE choice_id = '2127' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '687' WHERE choice_id = '2127' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '688' WHERE choice_id = '2237' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '688' WHERE choice_id = '2237' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '689' WHERE choice_id = '2322' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '689' WHERE choice_id = '2322' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';

-- Consultation rn3ib6tpe4yv3l7ofolyqh7d / question 20215
UPDATE consultation_results SET choice_id = '690' WHERE choice_id = '1606' AND question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '690' WHERE choice_id = '1606' AND question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '691' WHERE choice_id = '1805' AND question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '691' WHERE choice_id = '1805' AND question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '692' WHERE choice_id = '2128' AND question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '692' WHERE choice_id = '2128' AND question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '693' WHERE choice_id = '1984' AND question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '693' WHERE choice_id = '1984' AND question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '694' WHERE choice_id = '2238' AND question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '694' WHERE choice_id = '2238' AND question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';

-- Consultation rn3ib6tpe4yv3l7ofolyqh7d / question 20216
UPDATE consultation_results SET choice_id = '695' WHERE choice_id = '1806' AND question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '695' WHERE choice_id = '1806' AND question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '696' WHERE choice_id = '1985' AND question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '696' WHERE choice_id = '1985' AND question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '697' WHERE choice_id = '1607' AND question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '697' WHERE choice_id = '1607' AND question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '698' WHERE choice_id = '2129' AND question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '698' WHERE choice_id = '2129' AND question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '699' WHERE choice_id = '2239' AND question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '699' WHERE choice_id = '2239' AND question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 20217
UPDATE consultation_results SET choice_id = '703' WHERE choice_id = '1810' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '703' WHERE choice_id = '1810' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '704' WHERE choice_id = '1611' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '704' WHERE choice_id = '1611' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '706' WHERE choice_id = '1986' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '706' WHERE choice_id = '1986' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '707' WHERE choice_id = '2130' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '707' WHERE choice_id = '2130' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '708' WHERE choice_id = '2240' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '708' WHERE choice_id = '2240' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '709' WHERE choice_id = '2383' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '709' WHERE choice_id = '2383' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '710' WHERE choice_id = '2323' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '710' WHERE choice_id = '2323' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 20219
UPDATE consultation_results SET choice_id = '717' WHERE choice_id = '1613' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '717' WHERE choice_id = '1613' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '718' WHERE choice_id = '1812' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '718' WHERE choice_id = '1812' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '719' WHERE choice_id = '1988' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '719' WHERE choice_id = '1988' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '720' WHERE choice_id = '2132' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '720' WHERE choice_id = '2132' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '721' WHERE choice_id = '2242' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '721' WHERE choice_id = '2242' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '722' WHERE choice_id = '2324' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '722' WHERE choice_id = '2324' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '725' WHERE choice_id = '2384' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '725' WHERE choice_id = '2384' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 20218
UPDATE consultation_results SET choice_id = '712' WHERE choice_id = '1811' AND question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '712' WHERE choice_id = '1811' AND question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '713' WHERE choice_id = '1612' AND question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '713' WHERE choice_id = '1612' AND question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '714' WHERE choice_id = '1987' AND question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '714' WHERE choice_id = '1987' AND question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '715' WHERE choice_id = '2131' AND question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '715' WHERE choice_id = '2131' AND question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '716' WHERE choice_id = '2241' AND question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '716' WHERE choice_id = '2241' AND question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 20221
UPDATE consultation_results SET choice_id = '744' WHERE choice_id = '1818' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '744' WHERE choice_id = '1818' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '745' WHERE choice_id = '1619' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '745' WHERE choice_id = '1619' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '746' WHERE choice_id = '1993' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '746' WHERE choice_id = '1993' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '747' WHERE choice_id = '2244' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '747' WHERE choice_id = '2244' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '748' WHERE choice_id = '2134' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '748' WHERE choice_id = '2134' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '749' WHERE choice_id = '2326' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '749' WHERE choice_id = '2326' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '751' WHERE choice_id = '2386' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '751' WHERE choice_id = '2386' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 20220
UPDATE consultation_results SET choice_id = '737' WHERE choice_id = '1618' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '737' WHERE choice_id = '1618' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '738' WHERE choice_id = '1817' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '738' WHERE choice_id = '1817' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '739' WHERE choice_id = '1992' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '739' WHERE choice_id = '1992' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '740' WHERE choice_id = '2133' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '740' WHERE choice_id = '2133' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '741' WHERE choice_id = '2325' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '741' WHERE choice_id = '2325' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '742' WHERE choice_id = '2243' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '742' WHERE choice_id = '2243' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '743' WHERE choice_id = '2385' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '743' WHERE choice_id = '2385' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 20222
UPDATE consultation_results SET choice_id = '756' WHERE choice_id = '1620' AND question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '756' WHERE choice_id = '1620' AND question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '757' WHERE choice_id = '2245' AND question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '757' WHERE choice_id = '2245' AND question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '759' WHERE choice_id = '1819' AND question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '759' WHERE choice_id = '1819' AND question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '760' WHERE choice_id = '2135' AND question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '760' WHERE choice_id = '2135' AND question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '762' WHERE choice_id = '1994' AND question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '762' WHERE choice_id = '1994' AND question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation jf84upj9qutugwkce773m8dt / question 20187
UPDATE consultation_results SET choice_id = '1017' WHERE choice_id = '1727' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1017' WHERE choice_id = '1727' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1018' WHERE choice_id = '1528' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1018' WHERE choice_id = '1528' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1019' WHERE choice_id = '2079' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1019' WHERE choice_id = '2079' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1020' WHERE choice_id = '1921' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1020' WHERE choice_id = '1921' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1021' WHERE choice_id = '2302' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1021' WHERE choice_id = '2302' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1022' WHERE choice_id = '2205' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1022' WHERE choice_id = '2205' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';

-- Consultation jf84upj9qutugwkce773m8dt / question 20185
UPDATE consultation_results SET choice_id = '1023' WHERE choice_id = '1917' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1023' WHERE choice_id = '1917' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1024' WHERE choice_id = '1524' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1024' WHERE choice_id = '1524' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1025' WHERE choice_id = '1723' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1025' WHERE choice_id = '1723' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1026' WHERE choice_id = '2203' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1026' WHERE choice_id = '2203' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1027' WHERE choice_id = '2076' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1027' WHERE choice_id = '2076' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1028' WHERE choice_id = '2365' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1028' WHERE choice_id = '2365' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1029' WHERE choice_id = '2300' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1029' WHERE choice_id = '2300' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1030' WHERE choice_id = '2410' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1030' WHERE choice_id = '2410' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1031' WHERE choice_id = '2433' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1031' WHERE choice_id = '2433' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1036' WHERE choice_id = '2446' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1036' WHERE choice_id = '2446' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';

-- Consultation jf84upj9qutugwkce773m8dt / question 20192
UPDATE consultation_results SET choice_id = '1032' WHERE choice_id = '1543' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1032' WHERE choice_id = '1543' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1033' WHERE choice_id = '1742' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1033' WHERE choice_id = '1742' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1034' WHERE choice_id = '1936' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1034' WHERE choice_id = '1936' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1035' WHERE choice_id = '2091' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1035' WHERE choice_id = '2091' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1037' WHERE choice_id = '2306' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1037' WHERE choice_id = '2306' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1038' WHERE choice_id = '2369' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1038' WHERE choice_id = '2369' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1039' WHERE choice_id = '2210' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1039' WHERE choice_id = '2210' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1043' WHERE choice_id = '2413' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1043' WHERE choice_id = '2413' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';

-- Consultation jf84upj9qutugwkce773m8dt / question 20193
UPDATE consultation_results SET choice_id = '1070' WHERE choice_id = '1937' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1070' WHERE choice_id = '1937' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1071' WHERE choice_id = '1544' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1071' WHERE choice_id = '1544' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1072' WHERE choice_id = '1743' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1072' WHERE choice_id = '1743' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1073' WHERE choice_id = '2211' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1073' WHERE choice_id = '2211' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1074' WHERE choice_id = '2092' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1074' WHERE choice_id = '2092' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1075' WHERE choice_id = '2307' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1075' WHERE choice_id = '2307' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1076' WHERE choice_id = '2370' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1076' WHERE choice_id = '2370' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';

-- Consultation jf84upj9qutugwkce773m8dt / question 20188
UPDATE consultation_results SET choice_id = '1044' WHERE choice_id = '1529' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1044' WHERE choice_id = '1529' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1045' WHERE choice_id = '1922' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1045' WHERE choice_id = '1922' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1046' WHERE choice_id = '1728' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1046' WHERE choice_id = '1728' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1047' WHERE choice_id = '2080' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1047' WHERE choice_id = '2080' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1048' WHERE choice_id = '2206' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1048' WHERE choice_id = '2206' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1049' WHERE choice_id = '2303' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1049' WHERE choice_id = '2303' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';

-- Consultation jf84upj9qutugwkce773m8dt / question 20184
UPDATE consultation_results SET choice_id = '1050' WHERE choice_id = '1523' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1050' WHERE choice_id = '1523' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1051' WHERE choice_id = '1722' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1051' WHERE choice_id = '1722' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1052' WHERE choice_id = '1916' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1052' WHERE choice_id = '1916' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1053' WHERE choice_id = '2299' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1053' WHERE choice_id = '2299' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1054' WHERE choice_id = '2364' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1054' WHERE choice_id = '2364' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1055' WHERE choice_id = '2075' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1055' WHERE choice_id = '2075' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1056' WHERE choice_id = '2202' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1056' WHERE choice_id = '2202' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';

-- Consultation jf84upj9qutugwkce773m8dt / question 20194
UPDATE consultation_results SET choice_id = '1057' WHERE choice_id = '1545' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1057' WHERE choice_id = '1545' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1058' WHERE choice_id = '1744' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1058' WHERE choice_id = '1744' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1059' WHERE choice_id = '1938' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1059' WHERE choice_id = '1938' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1060' WHERE choice_id = '2212' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1060' WHERE choice_id = '2212' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1061' WHERE choice_id = '2093' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1061' WHERE choice_id = '2093' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1062' WHERE choice_id = '2308' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1062' WHERE choice_id = '2308' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1063' WHERE choice_id = '2371' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1063' WHERE choice_id = '2371' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1064' WHERE choice_id = '2414' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1064' WHERE choice_id = '2414' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1065' WHERE choice_id = '2435' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1065' WHERE choice_id = '2435' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1066' WHERE choice_id = '2448' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1066' WHERE choice_id = '2448' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1067' WHERE choice_id = '2454' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1067' WHERE choice_id = '2454' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1068' WHERE choice_id = '2459' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1068' WHERE choice_id = '2459' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1069' WHERE choice_id = '2463' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1069' WHERE choice_id = '2463' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 20137
UPDATE consultation_results SET choice_id = '1080' WHERE choice_id = '1436' AND question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1080' WHERE choice_id = '1436' AND question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1081' WHERE choice_id = '2006' AND question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1081' WHERE choice_id = '2006' AND question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1082' WHERE choice_id = '1635' AND question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1082' WHERE choice_id = '1635' AND question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1083' WHERE choice_id = '1834' AND question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1083' WHERE choice_id = '1834' AND question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1084' WHERE choice_id = '2147' AND question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1084' WHERE choice_id = '2147' AND question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 20138
UPDATE consultation_results SET choice_id = '1090' WHERE choice_id = '1437' AND question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1090' WHERE choice_id = '1437' AND question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1091' WHERE choice_id = '1636' AND question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1091' WHERE choice_id = '1636' AND question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1093' WHERE choice_id = '1835' AND question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1093' WHERE choice_id = '1835' AND question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1095' WHERE choice_id = '2007' AND question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1095' WHERE choice_id = '2007' AND question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1096' WHERE choice_id = '2148' AND question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1096' WHERE choice_id = '2148' AND question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 20178
UPDATE consultation_results SET choice_id = '1109' WHERE choice_id = '1706' AND question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1109' WHERE choice_id = '1706' AND question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1110' WHERE choice_id = '1507' AND question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1110' WHERE choice_id = '1507' AND question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1111' WHERE choice_id = '2062' AND question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1111' WHERE choice_id = '2062' AND question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1112' WHERE choice_id = '1901' AND question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1112' WHERE choice_id = '1901' AND question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1113' WHERE choice_id = '2194' AND question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1113' WHERE choice_id = '2194' AND question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 20143
UPDATE consultation_results SET choice_id = '1102' WHERE choice_id = '1447' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1102' WHERE choice_id = '1447' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1103' WHERE choice_id = '1845' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1103' WHERE choice_id = '1845' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1104' WHERE choice_id = '1646' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1104' WHERE choice_id = '1646' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1105' WHERE choice_id = '2014' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1105' WHERE choice_id = '2014' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1106' WHERE choice_id = '2154' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1106' WHERE choice_id = '2154' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1107' WHERE choice_id = '2259' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1107' WHERE choice_id = '2259' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 20179
UPDATE consultation_results SET choice_id = '1118' WHERE choice_id = '1509' AND question_id = '20179' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1118' WHERE choice_id = '1509' AND question_id = '20179' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1121' WHERE choice_id = '1903' AND question_id = '20179' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1121' WHERE choice_id = '1903' AND question_id = '20179' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1122' WHERE choice_id = '1708' AND question_id = '20179' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1122' WHERE choice_id = '1708' AND question_id = '20179' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1123' WHERE choice_id = '2064' AND question_id = '20179' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1123' WHERE choice_id = '2064' AND question_id = '20179' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 20203
UPDATE consultation_results SET choice_id = '1163' WHERE choice_id = '1580' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1163' WHERE choice_id = '1580' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1164' WHERE choice_id = '1779' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1164' WHERE choice_id = '1779' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1165' WHERE choice_id = '1963' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1165' WHERE choice_id = '1963' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1166' WHERE choice_id = '2113' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1166' WHERE choice_id = '2113' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1167' WHERE choice_id = '2225' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1167' WHERE choice_id = '2225' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1168' WHERE choice_id = '2377' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1168' WHERE choice_id = '2377' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1169' WHERE choice_id = '2314' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1169' WHERE choice_id = '2314' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1170' WHERE choice_id = '2417' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1170' WHERE choice_id = '2417' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1181' WHERE choice_id = '2437' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1181' WHERE choice_id = '2437' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 20204
UPDATE consultation_results SET choice_id = '1171' WHERE choice_id = '2315' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1171' WHERE choice_id = '2315' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1172' WHERE choice_id = '2438' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1172' WHERE choice_id = '2438' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1173' WHERE choice_id = '2418' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1173' WHERE choice_id = '2418' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1174' WHERE choice_id = '2378' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1174' WHERE choice_id = '2378' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1175' WHERE choice_id = '1581' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1175' WHERE choice_id = '1581' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1176' WHERE choice_id = '1780' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1176' WHERE choice_id = '1780' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1177' WHERE choice_id = '1964' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1177' WHERE choice_id = '1964' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1178' WHERE choice_id = '2114' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1178' WHERE choice_id = '2114' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1179' WHERE choice_id = '2449' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1179' WHERE choice_id = '2449' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1180' WHERE choice_id = '2226' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1180' WHERE choice_id = '2226' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 20205
UPDATE consultation_results SET choice_id = '1182' WHERE choice_id = '1582' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1182' WHERE choice_id = '1582' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1183' WHERE choice_id = '1781' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1183' WHERE choice_id = '1781' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1184' WHERE choice_id = '1965' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1184' WHERE choice_id = '1965' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1185' WHERE choice_id = '2115' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1185' WHERE choice_id = '2115' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1186' WHERE choice_id = '2227' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1186' WHERE choice_id = '2227' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1187' WHERE choice_id = '2316' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1187' WHERE choice_id = '2316' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation esg67bdrkzh0n29j2c858ryj / question 20186
UPDATE consultation_results SET choice_id = '1213' WHERE choice_id = '2077' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1213' WHERE choice_id = '2077' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1214' WHERE choice_id = '1918' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1214' WHERE choice_id = '1918' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1215' WHERE choice_id = '2204' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1215' WHERE choice_id = '2204' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1216' WHERE choice_id = '2301' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1216' WHERE choice_id = '2301' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1217' WHERE choice_id = '2366' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1217' WHERE choice_id = '2366' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1218' WHERE choice_id = '2411' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1218' WHERE choice_id = '2411' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1219' WHERE choice_id = '1525' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1219' WHERE choice_id = '1525' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1220' WHERE choice_id = '1724' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1220' WHERE choice_id = '1724' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';

-- Consultation esg67bdrkzh0n29j2c858ryj / question 20202
UPDATE consultation_results SET choice_id = '1228' WHERE choice_id = '1579' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1228' WHERE choice_id = '1579' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1229' WHERE choice_id = '1778' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1229' WHERE choice_id = '1778' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1230' WHERE choice_id = '1962' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1230' WHERE choice_id = '1962' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1231' WHERE choice_id = '2112' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1231' WHERE choice_id = '2112' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1232' WHERE choice_id = '2313' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1232' WHERE choice_id = '2313' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1233' WHERE choice_id = '2376' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1233' WHERE choice_id = '2376' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1234' WHERE choice_id = '2224' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1234' WHERE choice_id = '2224' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';

-- Consultation esg67bdrkzh0n29j2c858ryj / question 20195
UPDATE consultation_results SET choice_id = '1221' WHERE choice_id = '1554' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1221' WHERE choice_id = '1554' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1222' WHERE choice_id = '1753' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1222' WHERE choice_id = '1753' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1223' WHERE choice_id = '2097' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1223' WHERE choice_id = '2097' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1224' WHERE choice_id = '1945' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1224' WHERE choice_id = '1945' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1225' WHERE choice_id = '2309' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1225' WHERE choice_id = '2309' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1226' WHERE choice_id = '2214' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1226' WHERE choice_id = '2214' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1227' WHERE choice_id = '2372' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1227' WHERE choice_id = '2372' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';

-- Consultation esg67bdrkzh0n29j2c858ryj / question 20206
UPDATE consultation_results SET choice_id = '1235' WHERE choice_id = '2228' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1235' WHERE choice_id = '2228' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1250' WHERE choice_id = '2317' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1250' WHERE choice_id = '2317' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1251' WHERE choice_id = '2379' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1251' WHERE choice_id = '2379' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1252' WHERE choice_id = '2419' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1252' WHERE choice_id = '2419' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1253' WHERE choice_id = '2439' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1253' WHERE choice_id = '2439' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1254' WHERE choice_id = '2450' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1254' WHERE choice_id = '2450' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1255' WHERE choice_id = '2455' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1255' WHERE choice_id = '2455' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1256' WHERE choice_id = '2460' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1256' WHERE choice_id = '2460' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1257' WHERE choice_id = '2464' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1257' WHERE choice_id = '2464' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1258' WHERE choice_id = '2466' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1258' WHERE choice_id = '2466' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1259' WHERE choice_id = '2468' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1259' WHERE choice_id = '2468' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1260' WHERE choice_id = '2470' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1260' WHERE choice_id = '2470' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1261' WHERE choice_id = '1583' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1261' WHERE choice_id = '1583' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1262' WHERE choice_id = '1782' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1262' WHERE choice_id = '1782' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1263' WHERE choice_id = '1966' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1263' WHERE choice_id = '1966' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1264' WHERE choice_id = '2116' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1264' WHERE choice_id = '2116' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';

-- Consultation esg67bdrkzh0n29j2c858ryj / question 20208
UPDATE consultation_results SET choice_id = '1241' WHERE choice_id = '1784' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1241' WHERE choice_id = '1784' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1242' WHERE choice_id = '1585' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1242' WHERE choice_id = '1585' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1245' WHERE choice_id = '1968' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1245' WHERE choice_id = '1968' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1246' WHERE choice_id = '2118' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1246' WHERE choice_id = '2118' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1247' WHERE choice_id = '2230' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1247' WHERE choice_id = '2230' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1248' WHERE choice_id = '2319' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1248' WHERE choice_id = '2319' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1249' WHERE choice_id = '2381' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1249' WHERE choice_id = '2381' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';

-- Consultation esg67bdrkzh0n29j2c858ryj / question 20207
UPDATE consultation_results SET choice_id = '1236' WHERE choice_id = '1967' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1236' WHERE choice_id = '1967' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1237' WHERE choice_id = '1584' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1237' WHERE choice_id = '1584' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1238' WHERE choice_id = '1783' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1238' WHERE choice_id = '1783' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1239' WHERE choice_id = '2117' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1239' WHERE choice_id = '2117' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1240' WHERE choice_id = '2318' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1240' WHERE choice_id = '2318' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1243' WHERE choice_id = '2229' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1243' WHERE choice_id = '2229' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1244' WHERE choice_id = '2380' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1244' WHERE choice_id = '2380' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 20212
UPDATE consultation_results SET choice_id = '1303' WHERE choice_id = '1598' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1303' WHERE choice_id = '1598' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1304' WHERE choice_id = '1797' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1304' WHERE choice_id = '1797' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1305' WHERE choice_id = '1976' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1305' WHERE choice_id = '1976' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1306' WHERE choice_id = '2123' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1306' WHERE choice_id = '2123' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1307' WHERE choice_id = '2234' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1307' WHERE choice_id = '2234' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1308' WHERE choice_id = '2321' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1308' WHERE choice_id = '2321' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 20223
UPDATE consultation_results SET choice_id = '1316' WHERE choice_id = '1621' AND question_id = '20223' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1316' WHERE choice_id = '1621' AND question_id = '20223' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1318' WHERE choice_id = '2136' AND question_id = '20223' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1318' WHERE choice_id = '2136' AND question_id = '20223' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1320' WHERE choice_id = '1995' AND question_id = '20223' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1320' WHERE choice_id = '1995' AND question_id = '20223' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1321' WHERE choice_id = '1820' AND question_id = '20223' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1321' WHERE choice_id = '1820' AND question_id = '20223' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 20224
UPDATE consultation_results SET choice_id = '1319' WHERE choice_id = '1622' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1319' WHERE choice_id = '1622' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1322' WHERE choice_id = '1821' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1322' WHERE choice_id = '1821' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1323' WHERE choice_id = '2137' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1323' WHERE choice_id = '2137' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1324' WHERE choice_id = '2327' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1324' WHERE choice_id = '2327' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1325' WHERE choice_id = '2246' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1325' WHERE choice_id = '2246' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1326' WHERE choice_id = '1996' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1326' WHERE choice_id = '1996' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1327' WHERE choice_id = '2387' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1327' WHERE choice_id = '2387' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 20226
UPDATE consultation_results SET choice_id = '1338' WHERE choice_id = '1625' AND question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1338' WHERE choice_id = '1625' AND question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1340' WHERE choice_id = '1824' AND question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1340' WHERE choice_id = '1824' AND question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1341' WHERE choice_id = '1999' AND question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1341' WHERE choice_id = '1999' AND question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1342' WHERE choice_id = '2140' AND question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1342' WHERE choice_id = '2140' AND question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1343' WHERE choice_id = '2249' AND question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1343' WHERE choice_id = '2249' AND question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 20225
UPDATE consultation_results SET choice_id = '1334' WHERE choice_id = '1624' AND question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1334' WHERE choice_id = '1624' AND question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1335' WHERE choice_id = '1823' AND question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1335' WHERE choice_id = '1823' AND question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1336' WHERE choice_id = '1998' AND question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1336' WHERE choice_id = '1998' AND question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1337' WHERE choice_id = '2139' AND question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1337' WHERE choice_id = '2139' AND question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1339' WHERE choice_id = '2248' AND question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1339' WHERE choice_id = '2248' AND question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 20136
UPDATE consultation_results SET choice_id = '1356' WHERE choice_id = '1434' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1356' WHERE choice_id = '1434' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1364' WHERE choice_id = '2146' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1364' WHERE choice_id = '2146' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1365' WHERE choice_id = '2254' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1365' WHERE choice_id = '2254' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1366' WHERE choice_id = '2389' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1366' WHERE choice_id = '2389' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1367' WHERE choice_id = '2332' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1367' WHERE choice_id = '2332' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1368' WHERE choice_id = '1832' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1368' WHERE choice_id = '1832' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1369' WHERE choice_id = '2005' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1369' WHERE choice_id = '2005' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1370' WHERE choice_id = '1633' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1370' WHERE choice_id = '1633' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 20159
UPDATE consultation_results SET choice_id = '1357' WHERE choice_id = '1474' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1357' WHERE choice_id = '1474' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1358' WHERE choice_id = '1673' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1358' WHERE choice_id = '1673' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1359' WHERE choice_id = '1871' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1359' WHERE choice_id = '1871' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1360' WHERE choice_id = '2174' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1360' WHERE choice_id = '2174' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1361' WHERE choice_id = '2037' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1361' WHERE choice_id = '2037' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1362' WHERE choice_id = '2277' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1362' WHERE choice_id = '2277' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1363' WHERE choice_id = '2349' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1363' WHERE choice_id = '2349' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 20164
UPDATE consultation_results SET choice_id = '1371' WHERE choice_id = '1682' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1371' WHERE choice_id = '1682' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1372' WHERE choice_id = '1483' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1372' WHERE choice_id = '1483' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1386' WHERE choice_id = '1879' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1386' WHERE choice_id = '1879' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1387' WHERE choice_id = '2178' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1387' WHERE choice_id = '2178' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1388' WHERE choice_id = '2281' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1388' WHERE choice_id = '2281' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1389' WHERE choice_id = '2043' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1389' WHERE choice_id = '2043' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1390' WHERE choice_id = '2353' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1390' WHERE choice_id = '2353' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1391' WHERE choice_id = '2404' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1391' WHERE choice_id = '2404' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 20170
UPDATE consultation_results SET choice_id = '1373' WHERE choice_id = '1692' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1373' WHERE choice_id = '1692' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1374' WHERE choice_id = '1493' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1374' WHERE choice_id = '1493' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1375' WHERE choice_id = '1889' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1375' WHERE choice_id = '1889' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1376' WHERE choice_id = '2053' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1376' WHERE choice_id = '2053' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1377' WHERE choice_id = '2186' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1377' WHERE choice_id = '2186' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1378' WHERE choice_id = '2288' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1378' WHERE choice_id = '2288' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1380' WHERE choice_id = '2359' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1380' WHERE choice_id = '2359' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 20171
UPDATE consultation_results SET choice_id = '1379' WHERE choice_id = '1494' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1379' WHERE choice_id = '1494' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1381' WHERE choice_id = '1890' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1381' WHERE choice_id = '1890' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1382' WHERE choice_id = '1693' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1382' WHERE choice_id = '1693' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1383' WHERE choice_id = '2187' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1383' WHERE choice_id = '2187' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1384' WHERE choice_id = '2054' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1384' WHERE choice_id = '2054' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1385' WHERE choice_id = '2289' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1385' WHERE choice_id = '2289' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 20174
UPDATE consultation_results SET choice_id = '1392' WHERE choice_id = '1501' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1392' WHERE choice_id = '1501' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1393' WHERE choice_id = '1700' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1393' WHERE choice_id = '1700' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1394' WHERE choice_id = '1895' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1394' WHERE choice_id = '1895' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1395' WHERE choice_id = '2057' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1395' WHERE choice_id = '2057' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1396' WHERE choice_id = '2292' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1396' WHERE choice_id = '2292' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1397' WHERE choice_id = '2361' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1397' WHERE choice_id = '2361' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1398' WHERE choice_id = '2407' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1398' WHERE choice_id = '2407' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1399' WHERE choice_id = '2190' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1399' WHERE choice_id = '2190' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1400' WHERE choice_id = '2431' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1400' WHERE choice_id = '2431' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 20175
UPDATE consultation_results SET choice_id = '1401' WHERE choice_id = '1502' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1401' WHERE choice_id = '1502' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1402' WHERE choice_id = '1896' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1402' WHERE choice_id = '1896' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1403' WHERE choice_id = '1701' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1403' WHERE choice_id = '1701' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1404' WHERE choice_id = '2058' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1404' WHERE choice_id = '2058' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1405' WHERE choice_id = '2191' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1405' WHERE choice_id = '2191' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1406' WHERE choice_id = '2293' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1406' WHERE choice_id = '2293' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1407' WHERE choice_id = '2362' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1407' WHERE choice_id = '2362' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1408' WHERE choice_id = '2408' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1408' WHERE choice_id = '2408' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1409' WHERE choice_id = '2432' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1409' WHERE choice_id = '2432' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1411' WHERE choice_id = '2445' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1411' WHERE choice_id = '2445' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 20176
UPDATE consultation_results SET choice_id = '1410' WHERE choice_id = '1503' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1410' WHERE choice_id = '1503' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1412' WHERE choice_id = '1702' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1412' WHERE choice_id = '1702' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1413' WHERE choice_id = '1897' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1413' WHERE choice_id = '1897' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1414' WHERE choice_id = '2059' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1414' WHERE choice_id = '2059' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1415' WHERE choice_id = '2192' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1415' WHERE choice_id = '2192' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1419' WHERE choice_id = '2294' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1419' WHERE choice_id = '2294' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 20177
UPDATE consultation_results SET choice_id = '1424' WHERE choice_id = '1506' AND question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1424' WHERE choice_id = '1506' AND question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1425' WHERE choice_id = '1705' AND question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1425' WHERE choice_id = '1705' AND question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1426' WHERE choice_id = '1900' AND question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1426' WHERE choice_id = '1900' AND question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1427' WHERE choice_id = '2061' AND question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1427' WHERE choice_id = '2061' AND question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1428' WHERE choice_id = '2193' AND question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1428' WHERE choice_id = '2193' AND question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 40051
UPDATE consultation_results SET choice_id = '1' WHERE choice_id = '168' AND question_id = '40051' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1' WHERE choice_id = '168' AND question_id = '40051' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2' WHERE choice_id = '193' AND question_id = '40051' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2' WHERE choice_id = '193' AND question_id = '40051' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '3' WHERE choice_id = '232' AND question_id = '40051' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '3' WHERE choice_id = '232' AND question_id = '40051' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '4' WHERE choice_id = '218' AND question_id = '40051' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '4' WHERE choice_id = '218' AND question_id = '40051' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 40052
UPDATE consultation_results SET choice_id = '5' WHERE choice_id = '169' AND question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '5' WHERE choice_id = '169' AND question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '6' WHERE choice_id = '194' AND question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '6' WHERE choice_id = '194' AND question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '7' WHERE choice_id = '219' AND question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '7' WHERE choice_id = '219' AND question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '9' WHERE choice_id = '244' AND question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '9' WHERE choice_id = '244' AND question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '8' WHERE choice_id = '233' AND question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '8' WHERE choice_id = '233' AND question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 40055
UPDATE consultation_results SET choice_id = '10' WHERE choice_id = '172' AND question_id = '40055' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '10' WHERE choice_id = '172' AND question_id = '40055' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '11' WHERE choice_id = '197' AND question_id = '40055' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '11' WHERE choice_id = '197' AND question_id = '40055' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 40058
UPDATE consultation_results SET choice_id = '17' WHERE choice_id = '175' AND question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '17' WHERE choice_id = '175' AND question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '18' WHERE choice_id = '200' AND question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '18' WHERE choice_id = '200' AND question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '19' WHERE choice_id = '223' AND question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '19' WHERE choice_id = '223' AND question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '20' WHERE choice_id = '236' AND question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '20' WHERE choice_id = '236' AND question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '21' WHERE choice_id = '246' AND question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '21' WHERE choice_id = '246' AND question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 40059
UPDATE consultation_results SET choice_id = '22' WHERE choice_id = '176' AND question_id = '40059' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '22' WHERE choice_id = '176' AND question_id = '40059' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '23' WHERE choice_id = '201' AND question_id = '40059' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '23' WHERE choice_id = '201' AND question_id = '40059' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 40060
UPDATE consultation_results SET choice_id = '24' WHERE choice_id = '177' AND question_id = '40060' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '24' WHERE choice_id = '177' AND question_id = '40060' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '25' WHERE choice_id = '202' AND question_id = '40060' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '25' WHERE choice_id = '202' AND question_id = '40060' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation dy9l5rzk9r3ocurra7xanvi7 / question 30173
UPDATE consultation_results SET choice_id = '26' WHERE choice_id = '2086' AND question_id = '30173' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '26' WHERE choice_id = '2086' AND question_id = '30173' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '27' WHERE choice_id = '1536' AND question_id = '30173' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '27' WHERE choice_id = '1536' AND question_id = '30173' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';

-- Consultation dy9l5rzk9r3ocurra7xanvi7 / question 30174
UPDATE consultation_results SET choice_id = '28' WHERE choice_id = '1537' AND question_id = '30174' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '28' WHERE choice_id = '1537' AND question_id = '30174' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '29' WHERE choice_id = '1736' AND question_id = '30174' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '29' WHERE choice_id = '1736' AND question_id = '30174' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '30' WHERE choice_id = '1930' AND question_id = '30174' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '30' WHERE choice_id = '1930' AND question_id = '30174' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 40047
UPDATE consultation_results SET choice_id = '38' WHERE choice_id = '164' AND question_id = '40047' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '38' WHERE choice_id = '164' AND question_id = '40047' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '40' WHERE choice_id = '214' AND question_id = '40047' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '40' WHERE choice_id = '214' AND question_id = '40047' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '41' WHERE choice_id = '229' AND question_id = '40047' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '41' WHERE choice_id = '229' AND question_id = '40047' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '39' WHERE choice_id = '189' AND question_id = '40047' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '39' WHERE choice_id = '189' AND question_id = '40047' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation dy9l5rzk9r3ocurra7xanvi7 / question 30175
UPDATE consultation_results SET choice_id = '56' WHERE choice_id = '2469' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '56' WHERE choice_id = '2469' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '57' WHERE choice_id = '2471' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '57' WHERE choice_id = '2471' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '58' WHERE choice_id = '2472' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '58' WHERE choice_id = '2472' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '59' WHERE choice_id = '2473' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '59' WHERE choice_id = '2473' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '60' WHERE choice_id = '2474' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '60' WHERE choice_id = '2474' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '61' WHERE choice_id = '2475' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '61' WHERE choice_id = '2475' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '62' WHERE choice_id = '2476' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '62' WHERE choice_id = '2476' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '63' WHERE choice_id = '2477' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '63' WHERE choice_id = '2477' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '64' WHERE choice_id = '2478' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '64' WHERE choice_id = '2478' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '65' WHERE choice_id = '2088' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '65' WHERE choice_id = '2088' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '66' WHERE choice_id = '2208' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '66' WHERE choice_id = '2208' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '67' WHERE choice_id = '2305' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '67' WHERE choice_id = '2305' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '68' WHERE choice_id = '2368' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '68' WHERE choice_id = '2368' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '69' WHERE choice_id = '1738' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '69' WHERE choice_id = '1738' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '70' WHERE choice_id = '1539' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '70' WHERE choice_id = '1539' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '71' WHERE choice_id = '1932' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '71' WHERE choice_id = '1932' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';

-- Consultation pwr61j5qhpm6ekrf3liuw5bc / question 30131
UPDATE consultation_results SET choice_id = '72' WHERE choice_id = '1846' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '72' WHERE choice_id = '1846' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '73' WHERE choice_id = '1448' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '73' WHERE choice_id = '1448' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '74' WHERE choice_id = '2155' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '74' WHERE choice_id = '2155' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '75' WHERE choice_id = '2015' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '75' WHERE choice_id = '2015' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '76' WHERE choice_id = '1647' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '76' WHERE choice_id = '1647' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '77' WHERE choice_id = '2260' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '77' WHERE choice_id = '2260' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 40062
UPDATE consultation_results SET choice_id = '85' WHERE choice_id = '179' AND question_id = '40062' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '85' WHERE choice_id = '179' AND question_id = '40062' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '86' WHERE choice_id = '204' AND question_id = '40062' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '86' WHERE choice_id = '204' AND question_id = '40062' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 30134
UPDATE consultation_results SET choice_id = '112' WHERE choice_id = '1455' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '112' WHERE choice_id = '1455' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '113' WHERE choice_id = '2339' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '113' WHERE choice_id = '2339' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '114' WHERE choice_id = '1853' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '114' WHERE choice_id = '1853' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '115' WHERE choice_id = '1654' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '115' WHERE choice_id = '1654' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '116' WHERE choice_id = '2021' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '116' WHERE choice_id = '2021' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '117' WHERE choice_id = '2160' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '117' WHERE choice_id = '2160' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '118' WHERE choice_id = '2265' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '118' WHERE choice_id = '2265' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 30135
UPDATE consultation_results SET choice_id = '119' WHERE choice_id = '1854' AND question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '119' WHERE choice_id = '1854' AND question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '120' WHERE choice_id = '1456' AND question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '120' WHERE choice_id = '1456' AND question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '121' WHERE choice_id = '1655' AND question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '121' WHERE choice_id = '1655' AND question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '122' WHERE choice_id = '2022' AND question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '122' WHERE choice_id = '2022' AND question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '123' WHERE choice_id = '2161' AND question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '123' WHERE choice_id = '2161' AND question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 30136
UPDATE consultation_results SET choice_id = '138' WHERE choice_id = '1458' AND question_id = '30136' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '138' WHERE choice_id = '1458' AND question_id = '30136' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '139' WHERE choice_id = '1657' AND question_id = '30136' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '139' WHERE choice_id = '1657' AND question_id = '30136' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '140' WHERE choice_id = '2024' AND question_id = '30136' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '140' WHERE choice_id = '2024' AND question_id = '30136' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '141' WHERE choice_id = '1856' AND question_id = '30136' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '141' WHERE choice_id = '1856' AND question_id = '30136' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 30138
UPDATE consultation_results SET choice_id = '174' WHERE choice_id = '1663' AND question_id = '30138' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '174' WHERE choice_id = '1663' AND question_id = '30138' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '175' WHERE choice_id = '1464' AND question_id = '30138' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '175' WHERE choice_id = '1464' AND question_id = '30138' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '176' WHERE choice_id = '1862' AND question_id = '30138' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '176' WHERE choice_id = '1862' AND question_id = '30138' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 30139
UPDATE consultation_results SET choice_id = '182' WHERE choice_id = '1466' AND question_id = '30139' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '182' WHERE choice_id = '1466' AND question_id = '30139' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '184' WHERE choice_id = '1665' AND question_id = '30139' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '184' WHERE choice_id = '1665' AND question_id = '30139' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 30137
UPDATE consultation_results SET choice_id = '171' WHERE choice_id = '1662' AND question_id = '30137' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '171' WHERE choice_id = '1662' AND question_id = '30137' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '172' WHERE choice_id = '1861' AND question_id = '30137' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '172' WHERE choice_id = '1861' AND question_id = '30137' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '173' WHERE choice_id = '1463' AND question_id = '30137' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '173' WHERE choice_id = '1463' AND question_id = '30137' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 30142
UPDATE consultation_results SET choice_id = '200' WHERE choice_id = '1476' AND question_id = '30142' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '200' WHERE choice_id = '1476' AND question_id = '30142' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '201' WHERE choice_id = '1675' AND question_id = '30142' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '201' WHERE choice_id = '1675' AND question_id = '30142' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '202' WHERE choice_id = '1873' AND question_id = '30142' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '202' WHERE choice_id = '1873' AND question_id = '30142' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '203' WHERE choice_id = '2039' AND question_id = '30142' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '203' WHERE choice_id = '2039' AND question_id = '30142' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 30143
UPDATE consultation_results SET choice_id = '214' WHERE choice_id = '1479' AND question_id = '30143' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '214' WHERE choice_id = '1479' AND question_id = '30143' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '215' WHERE choice_id = '1678' AND question_id = '30143' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '215' WHERE choice_id = '1678' AND question_id = '30143' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '216' WHERE choice_id = '1876' AND question_id = '30143' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '216' WHERE choice_id = '1876' AND question_id = '30143' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 30144
UPDATE consultation_results SET choice_id = '217' WHERE choice_id = '1480' AND question_id = '30144' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '217' WHERE choice_id = '1480' AND question_id = '30144' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '218' WHERE choice_id = '1877' AND question_id = '30144' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '218' WHERE choice_id = '1877' AND question_id = '30144' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '219' WHERE choice_id = '1679' AND question_id = '30144' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '219' WHERE choice_id = '1679' AND question_id = '30144' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 30145
UPDATE consultation_results SET choice_id = '227' WHERE choice_id = '1482' AND question_id = '30145' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '227' WHERE choice_id = '1482' AND question_id = '30145' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '228' WHERE choice_id = '1681' AND question_id = '30145' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '228' WHERE choice_id = '1681' AND question_id = '30145' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 30150
UPDATE consultation_results SET choice_id = '242' WHERE choice_id = '1497' AND question_id = '30150' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '242' WHERE choice_id = '1497' AND question_id = '30150' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '243' WHERE choice_id = '1696' AND question_id = '30150' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '243' WHERE choice_id = '1696' AND question_id = '30150' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 30151
UPDATE consultation_results SET choice_id = '244' WHERE choice_id = '1697' AND question_id = '30151' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '244' WHERE choice_id = '1697' AND question_id = '30151' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '245' WHERE choice_id = '1893' AND question_id = '30151' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '245' WHERE choice_id = '1893' AND question_id = '30151' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '246' WHERE choice_id = '1498' AND question_id = '30151' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '246' WHERE choice_id = '1498' AND question_id = '30151' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 30153
UPDATE consultation_results SET choice_id = '248' WHERE choice_id = '1500' AND question_id = '30153' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '248' WHERE choice_id = '1500' AND question_id = '30153' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '250' WHERE choice_id = '1699' AND question_id = '30153' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '250' WHERE choice_id = '1699' AND question_id = '30153' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 30152
UPDATE consultation_results SET choice_id = '247' WHERE choice_id = '1499' AND question_id = '30152' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '247' WHERE choice_id = '1499' AND question_id = '30152' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '249' WHERE choice_id = '1894' AND question_id = '30152' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '249' WHERE choice_id = '1894' AND question_id = '30152' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '251' WHERE choice_id = '1698' AND question_id = '30152' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '251' WHERE choice_id = '1698' AND question_id = '30152' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 30123
UPDATE consultation_results SET choice_id = '264' WHERE choice_id = '1431' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '264' WHERE choice_id = '1431' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '265' WHERE choice_id = '1630' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '265' WHERE choice_id = '1630' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '266' WHERE choice_id = '1829' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '266' WHERE choice_id = '1829' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '267' WHERE choice_id = '2144' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '267' WHERE choice_id = '2144' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '268' WHERE choice_id = '2003' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '268' WHERE choice_id = '2003' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '269' WHERE choice_id = '2252' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '269' WHERE choice_id = '2252' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '270' WHERE choice_id = '2330' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '270' WHERE choice_id = '2330' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 30124
UPDATE consultation_results SET choice_id = '271' WHERE choice_id = '1830' AND question_id = '30124' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '271' WHERE choice_id = '1830' AND question_id = '30124' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '272' WHERE choice_id = '1432' AND question_id = '30124' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '272' WHERE choice_id = '1432' AND question_id = '30124' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '273' WHERE choice_id = '1631' AND question_id = '30124' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '273' WHERE choice_id = '1631' AND question_id = '30124' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 30127
UPDATE consultation_results SET choice_id = '302' WHERE choice_id = '1441' AND question_id = '30127' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '302' WHERE choice_id = '1441' AND question_id = '30127' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '303' WHERE choice_id = '1640' AND question_id = '30127' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '303' WHERE choice_id = '1640' AND question_id = '30127' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '304' WHERE choice_id = '1839' AND question_id = '30127' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '304' WHERE choice_id = '1839' AND question_id = '30127' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 30129
UPDATE consultation_results SET choice_id = '308' WHERE choice_id = '1443' AND question_id = '30129' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '308' WHERE choice_id = '1443' AND question_id = '30129' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '309' WHERE choice_id = '1642' AND question_id = '30129' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '309' WHERE choice_id = '1642' AND question_id = '30129' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '310' WHERE choice_id = '1841' AND question_id = '30129' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '310' WHERE choice_id = '1841' AND question_id = '30129' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 30128
UPDATE consultation_results SET choice_id = '305' WHERE choice_id = '1442' AND question_id = '30128' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '305' WHERE choice_id = '1442' AND question_id = '30128' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '306' WHERE choice_id = '1641' AND question_id = '30128' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '306' WHERE choice_id = '1641' AND question_id = '30128' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '307' WHERE choice_id = '1840' AND question_id = '30128' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '307' WHERE choice_id = '1840' AND question_id = '30128' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 30130
UPDATE consultation_results SET choice_id = '332' WHERE choice_id = '1446' AND question_id = '30130' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '332' WHERE choice_id = '1446' AND question_id = '30130' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '333' WHERE choice_id = '1645' AND question_id = '30130' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '333' WHERE choice_id = '1645' AND question_id = '30130' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '334' WHERE choice_id = '1844' AND question_id = '30130' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '334' WHERE choice_id = '1844' AND question_id = '30130' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '335' WHERE choice_id = '2013' AND question_id = '30130' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '335' WHERE choice_id = '2013' AND question_id = '30130' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation kmbc0ie5zsx94dejoiv1wcnv / question 30146
UPDATE consultation_results SET choice_id = '336' WHERE choice_id = '1484' AND question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '336' WHERE choice_id = '1484' AND question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '337' WHERE choice_id = '1683' AND question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '337' WHERE choice_id = '1683' AND question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '338' WHERE choice_id = '2044' AND question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '338' WHERE choice_id = '2044' AND question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '339' WHERE choice_id = '2179' AND question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '339' WHERE choice_id = '2179' AND question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '340' WHERE choice_id = '1880' AND question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '340' WHERE choice_id = '1880' AND question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation kmbc0ie5zsx94dejoiv1wcnv / question 30148
UPDATE consultation_results SET choice_id = '345' WHERE choice_id = '1685' AND question_id = '30148' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '345' WHERE choice_id = '1685' AND question_id = '30148' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '346' WHERE choice_id = '1486' AND question_id = '30148' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '346' WHERE choice_id = '1486' AND question_id = '30148' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '347' WHERE choice_id = '1882' AND question_id = '30148' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '347' WHERE choice_id = '1882' AND question_id = '30148' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '350' WHERE choice_id = '2046' AND question_id = '30148' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '350' WHERE choice_id = '2046' AND question_id = '30148' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation kmbc0ie5zsx94dejoiv1wcnv / question 30147
UPDATE consultation_results SET choice_id = '341' WHERE choice_id = '1684' AND question_id = '30147' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '341' WHERE choice_id = '1684' AND question_id = '30147' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '342' WHERE choice_id = '1881' AND question_id = '30147' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '342' WHERE choice_id = '1881' AND question_id = '30147' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '343' WHERE choice_id = '2045' AND question_id = '30147' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '343' WHERE choice_id = '2045' AND question_id = '30147' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '344' WHERE choice_id = '1485' AND question_id = '30147' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '344' WHERE choice_id = '1485' AND question_id = '30147' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation kmbc0ie5zsx94dejoiv1wcnv / question 30149
UPDATE consultation_results SET choice_id = '387' WHERE choice_id = '2287' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '387' WHERE choice_id = '2287' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '389' WHERE choice_id = '1691' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '389' WHERE choice_id = '1691' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '391' WHERE choice_id = '2185' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '391' WHERE choice_id = '2185' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '392' WHERE choice_id = '1888' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '392' WHERE choice_id = '1888' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '393' WHERE choice_id = '2052' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '393' WHERE choice_id = '2052' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '394' WHERE choice_id = '1492' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '394' WHERE choice_id = '1492' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation w5d0rrhdlqby9shpt4wdk5oj / question 30140
UPDATE consultation_results SET choice_id = '404' WHERE choice_id = '1667' AND question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '404' WHERE choice_id = '1667' AND question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '405' WHERE choice_id = '1468' AND question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '405' WHERE choice_id = '1468' AND question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '406' WHERE choice_id = '2169' AND question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '406' WHERE choice_id = '2169' AND question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '407' WHERE choice_id = '2031' AND question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '407' WHERE choice_id = '2031' AND question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '408' WHERE choice_id = '1865' AND question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '408' WHERE choice_id = '1865' AND question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';

-- Consultation w5d0rrhdlqby9shpt4wdk5oj / question 30141
UPDATE consultation_results SET choice_id = '409' WHERE choice_id = '1469' AND question_id = '30141' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '409' WHERE choice_id = '1469' AND question_id = '30141' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '411' WHERE choice_id = '2032' AND question_id = '30141' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '411' WHERE choice_id = '2032' AND question_id = '30141' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '412' WHERE choice_id = '1668' AND question_id = '30141' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '412' WHERE choice_id = '1668' AND question_id = '30141' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '415' WHERE choice_id = '1866' AND question_id = '30141' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '415' WHERE choice_id = '1866' AND question_id = '30141' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30166
UPDATE consultation_results SET choice_id = '443' WHERE choice_id = '1919' AND question_id = '30166' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '443' WHERE choice_id = '1919' AND question_id = '30166' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '444' WHERE choice_id = '1526' AND question_id = '30166' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '444' WHERE choice_id = '1526' AND question_id = '30166' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '445' WHERE choice_id = '1725' AND question_id = '30166' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '445' WHERE choice_id = '1725' AND question_id = '30166' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30167
UPDATE consultation_results SET choice_id = '446' WHERE choice_id = '1527' AND question_id = '30167' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '446' WHERE choice_id = '1527' AND question_id = '30167' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '447' WHERE choice_id = '1726' AND question_id = '30167' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '447' WHERE choice_id = '1726' AND question_id = '30167' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '448' WHERE choice_id = '1920' AND question_id = '30167' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '448' WHERE choice_id = '1920' AND question_id = '30167' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '449' WHERE choice_id = '2078' AND question_id = '30167' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '449' WHERE choice_id = '2078' AND question_id = '30167' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30180
UPDATE consultation_results SET choice_id = '457' WHERE choice_id = '1548' AND question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '457' WHERE choice_id = '1548' AND question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '458' WHERE choice_id = '1747' AND question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '458' WHERE choice_id = '1747' AND question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '459' WHERE choice_id = '1941' AND question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '459' WHERE choice_id = '1941' AND question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '460' WHERE choice_id = '2095' AND question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '460' WHERE choice_id = '2095' AND question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '461' WHERE choice_id = '2213' AND question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '461' WHERE choice_id = '2213' AND question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30179
UPDATE consultation_results SET choice_id = '453' WHERE choice_id = '1547' AND question_id = '30179' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '453' WHERE choice_id = '1547' AND question_id = '30179' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '454' WHERE choice_id = '1746' AND question_id = '30179' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '454' WHERE choice_id = '1746' AND question_id = '30179' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '456' WHERE choice_id = '1940' AND question_id = '30179' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '456' WHERE choice_id = '1940' AND question_id = '30179' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30178
UPDATE consultation_results SET choice_id = '450' WHERE choice_id = '1546' AND question_id = '30178' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '450' WHERE choice_id = '1546' AND question_id = '30178' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '451' WHERE choice_id = '1745' AND question_id = '30178' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '451' WHERE choice_id = '1745' AND question_id = '30178' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '452' WHERE choice_id = '1939' AND question_id = '30178' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '452' WHERE choice_id = '1939' AND question_id = '30178' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '455' WHERE choice_id = '2094' AND question_id = '30178' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '455' WHERE choice_id = '2094' AND question_id = '30178' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30182
UPDATE consultation_results SET choice_id = '464' WHERE choice_id = '1550' AND question_id = '30182' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '464' WHERE choice_id = '1550' AND question_id = '30182' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '465' WHERE choice_id = '1749' AND question_id = '30182' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '465' WHERE choice_id = '1749' AND question_id = '30182' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30181
UPDATE consultation_results SET choice_id = '462' WHERE choice_id = '1549' AND question_id = '30181' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '462' WHERE choice_id = '1549' AND question_id = '30181' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '463' WHERE choice_id = '1748' AND question_id = '30181' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '463' WHERE choice_id = '1748' AND question_id = '30181' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '466' WHERE choice_id = '1942' AND question_id = '30181' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '466' WHERE choice_id = '1942' AND question_id = '30181' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30183
UPDATE consultation_results SET choice_id = '467' WHERE choice_id = '1750' AND question_id = '30183' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '467' WHERE choice_id = '1750' AND question_id = '30183' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '468' WHERE choice_id = '1551' AND question_id = '30183' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '468' WHERE choice_id = '1551' AND question_id = '30183' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30186
UPDATE consultation_results SET choice_id = '476' WHERE choice_id = '1754' AND question_id = '30186' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '476' WHERE choice_id = '1754' AND question_id = '30186' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '477' WHERE choice_id = '1555' AND question_id = '30186' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '477' WHERE choice_id = '1555' AND question_id = '30186' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30185
UPDATE consultation_results SET choice_id = '472' WHERE choice_id = '1752' AND question_id = '30185' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '472' WHERE choice_id = '1752' AND question_id = '30185' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '473' WHERE choice_id = '1553' AND question_id = '30185' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '473' WHERE choice_id = '1553' AND question_id = '30185' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '474' WHERE choice_id = '1944' AND question_id = '30185' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '474' WHERE choice_id = '1944' AND question_id = '30185' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '475' WHERE choice_id = '2096' AND question_id = '30185' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '475' WHERE choice_id = '2096' AND question_id = '30185' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30188
UPDATE consultation_results SET choice_id = '480' WHERE choice_id = '1557' AND question_id = '30188' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '480' WHERE choice_id = '1557' AND question_id = '30188' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '481' WHERE choice_id = '1756' AND question_id = '30188' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '481' WHERE choice_id = '1756' AND question_id = '30188' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '482' WHERE choice_id = '1946' AND question_id = '30188' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '482' WHERE choice_id = '1946' AND question_id = '30188' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '483' WHERE choice_id = '2098' AND question_id = '30188' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '483' WHERE choice_id = '2098' AND question_id = '30188' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30184
UPDATE consultation_results SET choice_id = '469' WHERE choice_id = '1751' AND question_id = '30184' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '469' WHERE choice_id = '1751' AND question_id = '30184' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '470' WHERE choice_id = '1552' AND question_id = '30184' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '470' WHERE choice_id = '1552' AND question_id = '30184' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '471' WHERE choice_id = '1943' AND question_id = '30184' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '471' WHERE choice_id = '1943' AND question_id = '30184' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30187
UPDATE consultation_results SET choice_id = '478' WHERE choice_id = '1755' AND question_id = '30187' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '478' WHERE choice_id = '1755' AND question_id = '30187' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '479' WHERE choice_id = '1556' AND question_id = '30187' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '479' WHERE choice_id = '1556' AND question_id = '30187' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30189
UPDATE consultation_results SET choice_id = '493' WHERE choice_id = '1559' AND question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '493' WHERE choice_id = '1559' AND question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '494' WHERE choice_id = '1948' AND question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '494' WHERE choice_id = '1948' AND question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '495' WHERE choice_id = '2216' AND question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '495' WHERE choice_id = '2216' AND question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '496' WHERE choice_id = '1758' AND question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '496' WHERE choice_id = '1758' AND question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '497' WHERE choice_id = '2100' AND question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '497' WHERE choice_id = '2100' AND question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30190
UPDATE consultation_results SET choice_id = '499' WHERE choice_id = '1560' AND question_id = '30190' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '499' WHERE choice_id = '1560' AND question_id = '30190' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '500' WHERE choice_id = '1949' AND question_id = '30190' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '500' WHERE choice_id = '1949' AND question_id = '30190' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '502' WHERE choice_id = '1759' AND question_id = '30190' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '502' WHERE choice_id = '1759' AND question_id = '30190' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30191
UPDATE consultation_results SET choice_id = '498' WHERE choice_id = '1760' AND question_id = '30191' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '498' WHERE choice_id = '1760' AND question_id = '30191' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '501' WHERE choice_id = '1950' AND question_id = '30191' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '501' WHERE choice_id = '1950' AND question_id = '30191' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '503' WHERE choice_id = '2101' AND question_id = '30191' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '503' WHERE choice_id = '2101' AND question_id = '30191' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '504' WHERE choice_id = '1561' AND question_id = '30191' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '504' WHERE choice_id = '1561' AND question_id = '30191' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30193
UPDATE consultation_results SET choice_id = '507' WHERE choice_id = '1563' AND question_id = '30193' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '507' WHERE choice_id = '1563' AND question_id = '30193' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '508' WHERE choice_id = '1762' AND question_id = '30193' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '508' WHERE choice_id = '1762' AND question_id = '30193' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30192
UPDATE consultation_results SET choice_id = '505' WHERE choice_id = '1562' AND question_id = '30192' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '505' WHERE choice_id = '1562' AND question_id = '30192' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '506' WHERE choice_id = '1761' AND question_id = '30192' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '506' WHERE choice_id = '1761' AND question_id = '30192' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30194
UPDATE consultation_results SET choice_id = '510' WHERE choice_id = '1763' AND question_id = '30194' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '510' WHERE choice_id = '1763' AND question_id = '30194' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '511' WHERE choice_id = '1564' AND question_id = '30194' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '511' WHERE choice_id = '1564' AND question_id = '30194' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30197
UPDATE consultation_results SET choice_id = '522' WHERE choice_id = '1568' AND question_id = '30197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '522' WHERE choice_id = '1568' AND question_id = '30197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '523' WHERE choice_id = '1767' AND question_id = '30197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '523' WHERE choice_id = '1767' AND question_id = '30197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30195
UPDATE consultation_results SET choice_id = '513' WHERE choice_id = '1566' AND question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '513' WHERE choice_id = '1566' AND question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '514' WHERE choice_id = '2103' AND question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '514' WHERE choice_id = '2103' AND question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '517' WHERE choice_id = '1952' AND question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '517' WHERE choice_id = '1952' AND question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '518' WHERE choice_id = '2217' AND question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '518' WHERE choice_id = '2217' AND question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '519' WHERE choice_id = '1765' AND question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '519' WHERE choice_id = '1765' AND question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30198
UPDATE consultation_results SET choice_id = '524' WHERE choice_id = '2218' AND question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '524' WHERE choice_id = '2218' AND question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '525' WHERE choice_id = '2104' AND question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '525' WHERE choice_id = '2104' AND question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '526' WHERE choice_id = '1953' AND question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '526' WHERE choice_id = '1953' AND question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '527' WHERE choice_id = '1768' AND question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '527' WHERE choice_id = '1768' AND question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '528' WHERE choice_id = '1569' AND question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '528' WHERE choice_id = '1569' AND question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30196
UPDATE consultation_results SET choice_id = '520' WHERE choice_id = '1567' AND question_id = '30196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '520' WHERE choice_id = '1567' AND question_id = '30196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '521' WHERE choice_id = '1766' AND question_id = '30196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '521' WHERE choice_id = '1766' AND question_id = '30196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30199
UPDATE consultation_results SET choice_id = '529' WHERE choice_id = '1570' AND question_id = '30199' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '529' WHERE choice_id = '1570' AND question_id = '30199' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '530' WHERE choice_id = '1769' AND question_id = '30199' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '530' WHERE choice_id = '1769' AND question_id = '30199' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '531' WHERE choice_id = '1954' AND question_id = '30199' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '531' WHERE choice_id = '1954' AND question_id = '30199' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30204
UPDATE consultation_results SET choice_id = '535' WHERE choice_id = '1586' AND question_id = '30204' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '535' WHERE choice_id = '1586' AND question_id = '30204' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '540' WHERE choice_id = '1785' AND question_id = '30204' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '540' WHERE choice_id = '1785' AND question_id = '30204' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30201
UPDATE consultation_results SET choice_id = '536' WHERE choice_id = '1771' AND question_id = '30201' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '536' WHERE choice_id = '1771' AND question_id = '30201' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '537' WHERE choice_id = '1572' AND question_id = '30201' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '537' WHERE choice_id = '1572' AND question_id = '30201' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30200
UPDATE consultation_results SET choice_id = '532' WHERE choice_id = '1770' AND question_id = '30200' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '532' WHERE choice_id = '1770' AND question_id = '30200' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '533' WHERE choice_id = '1571' AND question_id = '30200' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '533' WHERE choice_id = '1571' AND question_id = '30200' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '534' WHERE choice_id = '1955' AND question_id = '30200' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '534' WHERE choice_id = '1955' AND question_id = '30200' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '538' WHERE choice_id = '2105' AND question_id = '30200' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '538' WHERE choice_id = '2105' AND question_id = '30200' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30208
UPDATE consultation_results SET choice_id = '562' WHERE choice_id = '1592' AND question_id = '30208' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '562' WHERE choice_id = '1592' AND question_id = '30208' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '563' WHERE choice_id = '1791' AND question_id = '30208' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '563' WHERE choice_id = '1791' AND question_id = '30208' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '564' WHERE choice_id = '1973' AND question_id = '30208' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '564' WHERE choice_id = '1973' AND question_id = '30208' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30205
UPDATE consultation_results SET choice_id = '552' WHERE choice_id = '1788' AND question_id = '30205' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '552' WHERE choice_id = '1788' AND question_id = '30205' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '553' WHERE choice_id = '1589' AND question_id = '30205' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '553' WHERE choice_id = '1589' AND question_id = '30205' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30206
UPDATE consultation_results SET choice_id = '554' WHERE choice_id = '2121' AND question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '554' WHERE choice_id = '2121' AND question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '555' WHERE choice_id = '1971' AND question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '555' WHERE choice_id = '1971' AND question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '556' WHERE choice_id = '2233' AND question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '556' WHERE choice_id = '2233' AND question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '557' WHERE choice_id = '1789' AND question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '557' WHERE choice_id = '1789' AND question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '558' WHERE choice_id = '1590' AND question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '558' WHERE choice_id = '1590' AND question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30207
UPDATE consultation_results SET choice_id = '559' WHERE choice_id = '1591' AND question_id = '30207' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '559' WHERE choice_id = '1591' AND question_id = '30207' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '560' WHERE choice_id = '1790' AND question_id = '30207' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '560' WHERE choice_id = '1790' AND question_id = '30207' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '561' WHERE choice_id = '1972' AND question_id = '30207' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '561' WHERE choice_id = '1972' AND question_id = '30207' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30209
UPDATE consultation_results SET choice_id = '565' WHERE choice_id = '1593' AND question_id = '30209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '565' WHERE choice_id = '1593' AND question_id = '30209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '566' WHERE choice_id = '1792' AND question_id = '30209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '566' WHERE choice_id = '1792' AND question_id = '30209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30210
UPDATE consultation_results SET choice_id = '567' WHERE choice_id = '1594' AND question_id = '30210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '567' WHERE choice_id = '1594' AND question_id = '30210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '569' WHERE choice_id = '1793' AND question_id = '30210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '569' WHERE choice_id = '1793' AND question_id = '30210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30211
UPDATE consultation_results SET choice_id = '568' WHERE choice_id = '1595' AND question_id = '30211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '568' WHERE choice_id = '1595' AND question_id = '30211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '570' WHERE choice_id = '1794' AND question_id = '30211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '570' WHERE choice_id = '1794' AND question_id = '30211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 30160
UPDATE consultation_results SET choice_id = '584' WHERE choice_id = '1513' AND question_id = '30160' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '584' WHERE choice_id = '1513' AND question_id = '30160' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '585' WHERE choice_id = '1712' AND question_id = '30160' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '585' WHERE choice_id = '1712' AND question_id = '30160' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '586' WHERE choice_id = '1906' AND question_id = '30160' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '586' WHERE choice_id = '1906' AND question_id = '30160' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '587' WHERE choice_id = '2066' AND question_id = '30160' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '587' WHERE choice_id = '2066' AND question_id = '30160' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 30158
UPDATE consultation_results SET choice_id = '577' WHERE choice_id = '1710' AND question_id = '30158' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '577' WHERE choice_id = '1710' AND question_id = '30158' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '578' WHERE choice_id = '1511' AND question_id = '30158' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '578' WHERE choice_id = '1511' AND question_id = '30158' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '579' WHERE choice_id = '1904' AND question_id = '30158' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '579' WHERE choice_id = '1904' AND question_id = '30158' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 30157
UPDATE consultation_results SET choice_id = '575' WHERE choice_id = '1510' AND question_id = '30157' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '575' WHERE choice_id = '1510' AND question_id = '30157' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '576' WHERE choice_id = '1709' AND question_id = '30157' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '576' WHERE choice_id = '1709' AND question_id = '30157' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 30159
UPDATE consultation_results SET choice_id = '580' WHERE choice_id = '1711' AND question_id = '30159' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '580' WHERE choice_id = '1711' AND question_id = '30159' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '581' WHERE choice_id = '1512' AND question_id = '30159' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '581' WHERE choice_id = '1512' AND question_id = '30159' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '582' WHERE choice_id = '1905' AND question_id = '30159' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '582' WHERE choice_id = '1905' AND question_id = '30159' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '583' WHERE choice_id = '2065' AND question_id = '30159' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '583' WHERE choice_id = '2065' AND question_id = '30159' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 30161
UPDATE consultation_results SET choice_id = '594' WHERE choice_id = '1515' AND question_id = '30161' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '594' WHERE choice_id = '1515' AND question_id = '30161' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '595' WHERE choice_id = '1714' AND question_id = '30161' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '595' WHERE choice_id = '1714' AND question_id = '30161' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '596' WHERE choice_id = '1908' AND question_id = '30161' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '596' WHERE choice_id = '1908' AND question_id = '30161' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 30162
UPDATE consultation_results SET choice_id = '597' WHERE choice_id = '1715' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '597' WHERE choice_id = '1715' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '598' WHERE choice_id = '1516' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '598' WHERE choice_id = '1516' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '599' WHERE choice_id = '1909' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '599' WHERE choice_id = '1909' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '600' WHERE choice_id = '2296' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '600' WHERE choice_id = '2296' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '601' WHERE choice_id = '2068' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '601' WHERE choice_id = '2068' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '602' WHERE choice_id = '2197' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '602' WHERE choice_id = '2197' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 30164
UPDATE consultation_results SET choice_id = '619' WHERE choice_id = '1520' AND question_id = '30164' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '619' WHERE choice_id = '1520' AND question_id = '30164' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '620' WHERE choice_id = '2072' AND question_id = '30164' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '620' WHERE choice_id = '2072' AND question_id = '30164' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '621' WHERE choice_id = '1719' AND question_id = '30164' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '621' WHERE choice_id = '1719' AND question_id = '30164' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '622' WHERE choice_id = '1913' AND question_id = '30164' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '622' WHERE choice_id = '1913' AND question_id = '30164' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 30165
UPDATE consultation_results SET choice_id = '629' WHERE choice_id = '1522' AND question_id = '30165' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '629' WHERE choice_id = '1522' AND question_id = '30165' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '632' WHERE choice_id = '1721' AND question_id = '30165' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '632' WHERE choice_id = '1721' AND question_id = '30165' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '633' WHERE choice_id = '2074' AND question_id = '30165' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '633' WHERE choice_id = '2074' AND question_id = '30165' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '634' WHERE choice_id = '1915' AND question_id = '30165' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '634' WHERE choice_id = '1915' AND question_id = '30165' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 30163
UPDATE consultation_results SET choice_id = '613' WHERE choice_id = '1519' AND question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '613' WHERE choice_id = '1519' AND question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '615' WHERE choice_id = '1912' AND question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '615' WHERE choice_id = '1912' AND question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '616' WHERE choice_id = '2200' AND question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '616' WHERE choice_id = '2200' AND question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '617' WHERE choice_id = '2071' AND question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '617' WHERE choice_id = '2071' AND question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '618' WHERE choice_id = '1718' AND question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '618' WHERE choice_id = '1718' AND question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation vkp77x0ognt41xyjicz9wwba / question 30202
UPDATE consultation_results SET choice_id = '635' WHERE choice_id = '1573' AND question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '635' WHERE choice_id = '1573' AND question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '636' WHERE choice_id = '1772' AND question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '636' WHERE choice_id = '1772' AND question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '637' WHERE choice_id = '1956' AND question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '637' WHERE choice_id = '1956' AND question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '638' WHERE choice_id = '2106' AND question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '638' WHERE choice_id = '2106' AND question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '639' WHERE choice_id = '2219' AND question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '639' WHERE choice_id = '2219' AND question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';

-- Consultation vkp77x0ognt41xyjicz9wwba / question 30203
UPDATE consultation_results SET choice_id = '664' WHERE choice_id = '1578' AND question_id = '30203' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '664' WHERE choice_id = '1578' AND question_id = '30203' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '666' WHERE choice_id = '2111' AND question_id = '30203' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '666' WHERE choice_id = '2111' AND question_id = '30203' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '667' WHERE choice_id = '1777' AND question_id = '30203' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '667' WHERE choice_id = '1777' AND question_id = '30203' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '668' WHERE choice_id = '1961' AND question_id = '30203' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '668' WHERE choice_id = '1961' AND question_id = '30203' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';

-- Consultation rn3ib6tpe4yv3l7ofolyqh7d / question 30215
UPDATE consultation_results SET choice_id = '674' WHERE choice_id = '1602' AND question_id = '30215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '674' WHERE choice_id = '1602' AND question_id = '30215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '675' WHERE choice_id = '1801' AND question_id = '30215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '675' WHERE choice_id = '1801' AND question_id = '30215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '677' WHERE choice_id = '1980' AND question_id = '30215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '677' WHERE choice_id = '1980' AND question_id = '30215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';

-- Consultation rn3ib6tpe4yv3l7ofolyqh7d / question 30217
UPDATE consultation_results SET choice_id = '681' WHERE choice_id = '1604' AND question_id = '30217' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '681' WHERE choice_id = '1604' AND question_id = '30217' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '682' WHERE choice_id = '1803' AND question_id = '30217' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '682' WHERE choice_id = '1803' AND question_id = '30217' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '683' WHERE choice_id = '1982' AND question_id = '30217' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '683' WHERE choice_id = '1982' AND question_id = '30217' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';

-- Consultation rn3ib6tpe4yv3l7ofolyqh7d / question 30216
UPDATE consultation_results SET choice_id = '676' WHERE choice_id = '1603' AND question_id = '30216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '676' WHERE choice_id = '1603' AND question_id = '30216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '678' WHERE choice_id = '1981' AND question_id = '30216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '678' WHERE choice_id = '1981' AND question_id = '30216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '679' WHERE choice_id = '1802' AND question_id = '30216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '679' WHERE choice_id = '1802' AND question_id = '30216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '680' WHERE choice_id = '2126' AND question_id = '30216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '680' WHERE choice_id = '2126' AND question_id = '30216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 30223
UPDATE consultation_results SET choice_id = '730' WHERE choice_id = '1616' AND question_id = '30223' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '730' WHERE choice_id = '1616' AND question_id = '30223' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '732' WHERE choice_id = '1815' AND question_id = '30223' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '732' WHERE choice_id = '1815' AND question_id = '30223' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '733' WHERE choice_id = '1991' AND question_id = '30223' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '733' WHERE choice_id = '1991' AND question_id = '30223' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 30222
UPDATE consultation_results SET choice_id = '728' WHERE choice_id = '1814' AND question_id = '30222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '728' WHERE choice_id = '1814' AND question_id = '30222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '729' WHERE choice_id = '1615' AND question_id = '30222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '729' WHERE choice_id = '1615' AND question_id = '30222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '731' WHERE choice_id = '1990' AND question_id = '30222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '731' WHERE choice_id = '1990' AND question_id = '30222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 30221
UPDATE consultation_results SET choice_id = '724' WHERE choice_id = '1614' AND question_id = '30221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '724' WHERE choice_id = '1614' AND question_id = '30221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '726' WHERE choice_id = '1813' AND question_id = '30221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '726' WHERE choice_id = '1813' AND question_id = '30221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '727' WHERE choice_id = '1989' AND question_id = '30221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '727' WHERE choice_id = '1989' AND question_id = '30221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 30224
UPDATE consultation_results SET choice_id = '734' WHERE choice_id = '1617' AND question_id = '30224' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '734' WHERE choice_id = '1617' AND question_id = '30224' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '735' WHERE choice_id = '1816' AND question_id = '30224' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '735' WHERE choice_id = '1816' AND question_id = '30224' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 30218
UPDATE consultation_results SET choice_id = '763' WHERE choice_id = '1807' AND question_id = '30218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '763' WHERE choice_id = '1807' AND question_id = '30218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '765' WHERE choice_id = '1608' AND question_id = '30218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '765' WHERE choice_id = '1608' AND question_id = '30218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 30219
UPDATE consultation_results SET choice_id = '764' WHERE choice_id = '1609' AND question_id = '30219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '764' WHERE choice_id = '1609' AND question_id = '30219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '766' WHERE choice_id = '1808' AND question_id = '30219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '766' WHERE choice_id = '1808' AND question_id = '30219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 30220
UPDATE consultation_results SET choice_id = '767' WHERE choice_id = '1809' AND question_id = '30220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '767' WHERE choice_id = '1809' AND question_id = '30220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '768' WHERE choice_id = '1610' AND question_id = '30220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '768' WHERE choice_id = '1610' AND question_id = '30220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation jf84upj9qutugwkce773m8dt / question 30177
UPDATE consultation_results SET choice_id = '1013' WHERE choice_id = '1741' AND question_id = '30177' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1013' WHERE choice_id = '1741' AND question_id = '30177' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1014' WHERE choice_id = '1542' AND question_id = '30177' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1014' WHERE choice_id = '1542' AND question_id = '30177' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1015' WHERE choice_id = '2090' AND question_id = '30177' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1015' WHERE choice_id = '2090' AND question_id = '30177' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1016' WHERE choice_id = '1935' AND question_id = '30177' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1016' WHERE choice_id = '1935' AND question_id = '30177' AND consultation_id = 'jf84upj9qutugwkce773m8dt';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 30125
UPDATE consultation_results SET choice_id = '1077' WHERE choice_id = '1435' AND question_id = '30125' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1077' WHERE choice_id = '1435' AND question_id = '30125' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1078' WHERE choice_id = '1634' AND question_id = '30125' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1078' WHERE choice_id = '1634' AND question_id = '30125' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1079' WHERE choice_id = '1833' AND question_id = '30125' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1079' WHERE choice_id = '1833' AND question_id = '30125' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 30126
UPDATE consultation_results SET choice_id = '1097' WHERE choice_id = '1438' AND question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1097' WHERE choice_id = '1438' AND question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1098' WHERE choice_id = '1637' AND question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1098' WHERE choice_id = '1637' AND question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1099' WHERE choice_id = '1836' AND question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1099' WHERE choice_id = '1836' AND question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1100' WHERE choice_id = '2008' AND question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1100' WHERE choice_id = '2008' AND question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1101' WHERE choice_id = '2149' AND question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1101' WHERE choice_id = '2149' AND question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 30156
UPDATE consultation_results SET choice_id = '1114' WHERE choice_id = '1508' AND question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1114' WHERE choice_id = '1508' AND question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1115' WHERE choice_id = '1707' AND question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1115' WHERE choice_id = '1707' AND question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1116' WHERE choice_id = '1902' AND question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1116' WHERE choice_id = '1902' AND question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1117' WHERE choice_id = '2063' AND question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1117' WHERE choice_id = '2063' AND question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1119' WHERE choice_id = '2195' AND question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1119' WHERE choice_id = '2195' AND question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 30212
UPDATE consultation_results SET choice_id = '1265' WHERE choice_id = '1975' AND question_id = '30212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1265' WHERE choice_id = '1975' AND question_id = '30212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1266' WHERE choice_id = '1597' AND question_id = '30212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1266' WHERE choice_id = '1597' AND question_id = '30212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1267' WHERE choice_id = '1796' AND question_id = '30212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1267' WHERE choice_id = '1796' AND question_id = '30212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 30214
UPDATE consultation_results SET choice_id = '1313' WHERE choice_id = '1600' AND question_id = '30214' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1313' WHERE choice_id = '1600' AND question_id = '30214' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1315' WHERE choice_id = '1799' AND question_id = '30214' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1315' WHERE choice_id = '1799' AND question_id = '30214' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1317' WHERE choice_id = '1978' AND question_id = '30214' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1317' WHERE choice_id = '1978' AND question_id = '30214' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 30225
UPDATE consultation_results SET choice_id = '1328' WHERE choice_id = '1623' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1328' WHERE choice_id = '1623' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1329' WHERE choice_id = '1822' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1329' WHERE choice_id = '1822' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1330' WHERE choice_id = '1997' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1330' WHERE choice_id = '1997' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1331' WHERE choice_id = '2247' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1331' WHERE choice_id = '2247' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1332' WHERE choice_id = '2328' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1332' WHERE choice_id = '2328' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1333' WHERE choice_id = '2138' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1333' WHERE choice_id = '2138' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 30213
UPDATE consultation_results SET choice_id = '1309' WHERE choice_id = '1599' AND question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1309' WHERE choice_id = '1599' AND question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1310' WHERE choice_id = '1798' AND question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1310' WHERE choice_id = '1798' AND question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1311' WHERE choice_id = '1977' AND question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1311' WHERE choice_id = '1977' AND question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1312' WHERE choice_id = '2235' AND question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1312' WHERE choice_id = '2235' AND question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1314' WHERE choice_id = '2124' AND question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1314' WHERE choice_id = '2124' AND question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 30227
UPDATE consultation_results SET choice_id = '1348' WHERE choice_id = '1826' AND question_id = '30227' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1348' WHERE choice_id = '1826' AND question_id = '30227' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1349' WHERE choice_id = '1627' AND question_id = '30227' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1349' WHERE choice_id = '1627' AND question_id = '30227' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 30226
UPDATE consultation_results SET choice_id = '1351' WHERE choice_id = '1626' AND question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1351' WHERE choice_id = '1626' AND question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1352' WHERE choice_id = '2000' AND question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1352' WHERE choice_id = '2000' AND question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1353' WHERE choice_id = '2250' AND question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1353' WHERE choice_id = '2250' AND question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1354' WHERE choice_id = '1825' AND question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1354' WHERE choice_id = '1825' AND question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1355' WHERE choice_id = '2141' AND question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1355' WHERE choice_id = '2141' AND question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 30155
UPDATE consultation_results SET choice_id = '1420' WHERE choice_id = '1505' AND question_id = '30155' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1420' WHERE choice_id = '1505' AND question_id = '30155' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1422' WHERE choice_id = '1704' AND question_id = '30155' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1422' WHERE choice_id = '1704' AND question_id = '30155' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1423' WHERE choice_id = '1899' AND question_id = '30155' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1423' WHERE choice_id = '1899' AND question_id = '30155' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 30154
UPDATE consultation_results SET choice_id = '1416' WHERE choice_id = '1504' AND question_id = '30154' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1416' WHERE choice_id = '1504' AND question_id = '30154' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1417' WHERE choice_id = '1703' AND question_id = '30154' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1417' WHERE choice_id = '1703' AND question_id = '30154' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1418' WHERE choice_id = '1898' AND question_id = '30154' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1418' WHERE choice_id = '1898' AND question_id = '30154' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1421' WHERE choice_id = '2060' AND question_id = '30154' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1421' WHERE choice_id = '2060' AND question_id = '30154' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- ============================================================
-- ROLLBACK ÉTAPE 1 : Restauration des anciens question_id
-- (à exécuter APRÈS le rollback des choice_id ci-dessus)
-- ============================================================

-- Consultation dy9l5rzk9r3ocurra7xanvi7
UPDATE consultation_results SET question_id = '20000' WHERE question_id = '20189' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '20000' WHERE question_id = '20189' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '20001' WHERE question_id = '20190' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '20001' WHERE question_id = '20190' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '20002' WHERE question_id = '20191' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '20002' WHERE question_id = '20191' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '30000' WHERE question_id = '30168' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '30000' WHERE question_id = '30168' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '30001' WHERE question_id = '30169' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '30001' WHERE question_id = '30169' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '30002' WHERE question_id = '30170' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '30002' WHERE question_id = '30170' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '30003' WHERE question_id = '30171' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '30003' WHERE question_id = '30171' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '30004' WHERE question_id = '30172' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '30004' WHERE question_id = '30172' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '30005' WHERE question_id = '30173' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '30005' WHERE question_id = '30173' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '30006' WHERE question_id = '30174' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '30006' WHERE question_id = '30174' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '30007' WHERE question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '30007' WHERE question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '30008' WHERE question_id = '30176' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '30008' WHERE question_id = '30176' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2
UPDATE consultation_results SET question_id = '20007' WHERE question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '20007' WHERE question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '20008' WHERE question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '20008' WHERE question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '20009' WHERE question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '20009' WHERE question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '20010' WHERE question_id = '20151' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '20010' WHERE question_id = '20151' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '20011' WHERE question_id = '20150' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '20011' WHERE question_id = '20150' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '20012' WHERE question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '20012' WHERE question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '20013' WHERE question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '20013' WHERE question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '30012' WHERE question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '30012' WHERE question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '30013' WHERE question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '30013' WHERE question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '30014' WHERE question_id = '30136' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '30014' WHERE question_id = '30136' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '30015' WHERE question_id = '30138' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '30015' WHERE question_id = '30138' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '30016' WHERE question_id = '30139' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '30016' WHERE question_id = '30139' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '30017' WHERE question_id = '30137' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '30017' WHERE question_id = '30137' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '40000' WHERE question_id = '40051' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '40000' WHERE question_id = '40051' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation pwr61j5qhpm6ekrf3liuw5bc
UPDATE consultation_results SET question_id = '20003' WHERE question_id = '20145' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET question_id = '20003' WHERE question_id = '20145' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET question_id = '20004' WHERE question_id = '20144' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET question_id = '20004' WHERE question_id = '20144' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET question_id = '20005' WHERE question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET question_id = '20005' WHERE question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET question_id = '20006' WHERE question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET question_id = '20006' WHERE question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET question_id = '30009' WHERE question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET question_id = '30009' WHERE question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET question_id = '30010' WHERE question_id = '30132' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET question_id = '30010' WHERE question_id = '30132' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET question_id = '30011' WHERE question_id = '30133' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET question_id = '30011' WHERE question_id = '30133' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';

-- Consultation vciluo7jt2hiavx7wuqbfwh1
UPDATE consultation_results SET question_id = '20014' WHERE question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '20014' WHERE question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '20015' WHERE question_id = '20161' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '20015' WHERE question_id = '20161' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '20016' WHERE question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '20016' WHERE question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '20017' WHERE question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '20017' WHERE question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '20018' WHERE question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '20018' WHERE question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '20019' WHERE question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '20019' WHERE question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '30018' WHERE question_id = '30142' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '30018' WHERE question_id = '30142' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '30019' WHERE question_id = '30143' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '30019' WHERE question_id = '30143' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '30020' WHERE question_id = '30144' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '30020' WHERE question_id = '30144' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '30021' WHERE question_id = '30145' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '30021' WHERE question_id = '30145' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '30022' WHERE question_id = '30150' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '30022' WHERE question_id = '30150' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '30023' WHERE question_id = '30151' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '30023' WHERE question_id = '30151' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '30024' WHERE question_id = '30153' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '30024' WHERE question_id = '30153' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '30025' WHERE question_id = '30152' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '30025' WHERE question_id = '30152' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '40001' WHERE question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '40001' WHERE question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '40002' WHERE question_id = '40055' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '40002' WHERE question_id = '40055' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo
UPDATE consultation_results SET question_id = '20020' WHERE question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '20020' WHERE question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '20021' WHERE question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '20021' WHERE question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '20022' WHERE question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '20022' WHERE question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '20023' WHERE question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '20023' WHERE question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '20024' WHERE question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '20024' WHERE question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '20025' WHERE question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '20025' WHERE question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '20026' WHERE question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '20026' WHERE question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '30026' WHERE question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '30026' WHERE question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '30027' WHERE question_id = '30124' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '30027' WHERE question_id = '30124' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '30028' WHERE question_id = '30127' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '30028' WHERE question_id = '30127' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '30029' WHERE question_id = '30129' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '30029' WHERE question_id = '30129' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '30030' WHERE question_id = '30128' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '30030' WHERE question_id = '30128' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '30031' WHERE question_id = '30130' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '30031' WHERE question_id = '30130' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation kmbc0ie5zsx94dejoiv1wcnv
UPDATE consultation_results SET question_id = '20028' WHERE question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET question_id = '20028' WHERE question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET question_id = '20029' WHERE question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET question_id = '20029' WHERE question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET question_id = '20030' WHERE question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET question_id = '20030' WHERE question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET question_id = '20031' WHERE question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET question_id = '20031' WHERE question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET question_id = '20032' WHERE question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET question_id = '20032' WHERE question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET question_id = '30032' WHERE question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET question_id = '30032' WHERE question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET question_id = '30033' WHERE question_id = '30148' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET question_id = '30033' WHERE question_id = '30148' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET question_id = '30034' WHERE question_id = '30147' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET question_id = '30034' WHERE question_id = '30147' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET question_id = '30035' WHERE question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET question_id = '30035' WHERE question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET question_id = '40003' WHERE question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET question_id = '40003' WHERE question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation w5d0rrhdlqby9shpt4wdk5oj
UPDATE consultation_results SET question_id = '20033' WHERE question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET question_id = '20033' WHERE question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET question_id = '20034' WHERE question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET question_id = '20034' WHERE question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET question_id = '20035' WHERE question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET question_id = '20035' WHERE question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET question_id = '20036' WHERE question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET question_id = '20036' WHERE question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET question_id = '30036' WHERE question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET question_id = '30036' WHERE question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET question_id = '30037' WHERE question_id = '30141' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET question_id = '30037' WHERE question_id = '30141' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';

-- Consultation wv0ul09acpw4f38v6s4uudqn
UPDATE consultation_results SET question_id = '20037' WHERE question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '20037' WHERE question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '20038' WHERE question_id = '20197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '20038' WHERE question_id = '20197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '20039' WHERE question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '20039' WHERE question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '20040' WHERE question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '20040' WHERE question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '20041' WHERE question_id = '20211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '20041' WHERE question_id = '20211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30038' WHERE question_id = '30166' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30038' WHERE question_id = '30166' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30039' WHERE question_id = '30167' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30039' WHERE question_id = '30167' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30040' WHERE question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30040' WHERE question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30041' WHERE question_id = '30179' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30041' WHERE question_id = '30179' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30042' WHERE question_id = '30178' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30042' WHERE question_id = '30178' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30043' WHERE question_id = '30182' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30043' WHERE question_id = '30182' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30044' WHERE question_id = '30181' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30044' WHERE question_id = '30181' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30045' WHERE question_id = '30183' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30045' WHERE question_id = '30183' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30046' WHERE question_id = '30186' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30046' WHERE question_id = '30186' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30047' WHERE question_id = '30185' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30047' WHERE question_id = '30185' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30048' WHERE question_id = '30188' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30048' WHERE question_id = '30188' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30049' WHERE question_id = '30184' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30049' WHERE question_id = '30184' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30050' WHERE question_id = '30187' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30050' WHERE question_id = '30187' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30051' WHERE question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30051' WHERE question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30052' WHERE question_id = '30190' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30052' WHERE question_id = '30190' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30053' WHERE question_id = '30191' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30053' WHERE question_id = '30191' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30054' WHERE question_id = '30193' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30054' WHERE question_id = '30193' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30055' WHERE question_id = '30192' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30055' WHERE question_id = '30192' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30056' WHERE question_id = '30194' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30056' WHERE question_id = '30194' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30057' WHERE question_id = '30197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30057' WHERE question_id = '30197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30058' WHERE question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30058' WHERE question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30059' WHERE question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30059' WHERE question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30060' WHERE question_id = '30196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30060' WHERE question_id = '30196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30061' WHERE question_id = '30199' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30061' WHERE question_id = '30199' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30062' WHERE question_id = '30204' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30062' WHERE question_id = '30204' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30063' WHERE question_id = '30201' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30063' WHERE question_id = '30201' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30064' WHERE question_id = '30200' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30064' WHERE question_id = '30200' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30065' WHERE question_id = '30208' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30065' WHERE question_id = '30208' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30066' WHERE question_id = '30205' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30066' WHERE question_id = '30205' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30067' WHERE question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30067' WHERE question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30068' WHERE question_id = '30207' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30068' WHERE question_id = '30207' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30069' WHERE question_id = '30209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30069' WHERE question_id = '30209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30070' WHERE question_id = '30210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30070' WHERE question_id = '30210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30071' WHERE question_id = '30211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30071' WHERE question_id = '30211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '40004' WHERE question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '40004' WHERE question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '40005' WHERE question_id = '40059' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '40005' WHERE question_id = '40059' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '40006' WHERE question_id = '40060' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '40006' WHERE question_id = '40060' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation g8w8ivfieyvs3ajinnbk4015
UPDATE consultation_results SET question_id = '20042' WHERE question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '20042' WHERE question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '20043' WHERE question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '20043' WHERE question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '20044' WHERE question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '20044' WHERE question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '20045' WHERE question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '20045' WHERE question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '30072' WHERE question_id = '30160' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '30072' WHERE question_id = '30160' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '30073' WHERE question_id = '30158' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '30073' WHERE question_id = '30158' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '30074' WHERE question_id = '30157' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '30074' WHERE question_id = '30157' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '30075' WHERE question_id = '30159' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '30075' WHERE question_id = '30159' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '30076' WHERE question_id = '30161' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '30076' WHERE question_id = '30161' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '30077' WHERE question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '30077' WHERE question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '30078' WHERE question_id = '30164' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '30078' WHERE question_id = '30164' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '30079' WHERE question_id = '30165' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '30079' WHERE question_id = '30165' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '30080' WHERE question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '30080' WHERE question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation vkp77x0ognt41xyjicz9wwba
UPDATE consultation_results SET question_id = '20046' WHERE question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET question_id = '20046' WHERE question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET question_id = '20047' WHERE question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET question_id = '20047' WHERE question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET question_id = '20048' WHERE question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET question_id = '20048' WHERE question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET question_id = '20049' WHERE question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET question_id = '20049' WHERE question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET question_id = '30081' WHERE question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET question_id = '30081' WHERE question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET question_id = '30082' WHERE question_id = '30203' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET question_id = '30082' WHERE question_id = '30203' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';

-- Consultation rn3ib6tpe4yv3l7ofolyqh7d
UPDATE consultation_results SET question_id = '20050' WHERE question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET question_id = '20050' WHERE question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET question_id = '20051' WHERE question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET question_id = '20051' WHERE question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET question_id = '20052' WHERE question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET question_id = '20052' WHERE question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET question_id = '20053' WHERE question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET question_id = '20053' WHERE question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET question_id = '30083' WHERE question_id = '30215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET question_id = '30083' WHERE question_id = '30215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET question_id = '30084' WHERE question_id = '30217' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET question_id = '30084' WHERE question_id = '30217' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET question_id = '30085' WHERE question_id = '30216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET question_id = '30085' WHERE question_id = '30216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';

-- Consultation ei80m02y5koialuq4e30n4q6
UPDATE consultation_results SET question_id = '20054' WHERE question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '20054' WHERE question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '20055' WHERE question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '20055' WHERE question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '20056' WHERE question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '20056' WHERE question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '20058' WHERE question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '20058' WHERE question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '20059' WHERE question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '20059' WHERE question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '20060' WHERE question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '20060' WHERE question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '30087' WHERE question_id = '30223' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '30087' WHERE question_id = '30223' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '30088' WHERE question_id = '30222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '30088' WHERE question_id = '30222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '30089' WHERE question_id = '30221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '30089' WHERE question_id = '30221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '30090' WHERE question_id = '30224' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '30090' WHERE question_id = '30224' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '30091' WHERE question_id = '30218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '30091' WHERE question_id = '30218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '30092' WHERE question_id = '30219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '30092' WHERE question_id = '30219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '30093' WHERE question_id = '30220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '30093' WHERE question_id = '30220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation jf84upj9qutugwkce773m8dt
UPDATE consultation_results SET question_id = '20084' WHERE question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET question_id = '20084' WHERE question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET question_id = '20085' WHERE question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET question_id = '20085' WHERE question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET question_id = '20086' WHERE question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET question_id = '20086' WHERE question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET question_id = '20087' WHERE question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET question_id = '20087' WHERE question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET question_id = '20088' WHERE question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET question_id = '20088' WHERE question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET question_id = '20089' WHERE question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET question_id = '20089' WHERE question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET question_id = '20090' WHERE question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET question_id = '20090' WHERE question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET question_id = '30105' WHERE question_id = '30177' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET question_id = '30105' WHERE question_id = '30177' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET question_id = '40010' WHERE question_id = '40056' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET question_id = '40010' WHERE question_id = '40056' AND consultation_id = 'jf84upj9qutugwkce773m8dt';

-- Consultation hxec8jg448kdtr5uy4jhxt8y
UPDATE consultation_results SET question_id = '20091' WHERE question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '20091' WHERE question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '20092' WHERE question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '20092' WHERE question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '20093' WHERE question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '20093' WHERE question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '20094' WHERE question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '20094' WHERE question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '20095' WHERE question_id = '20179' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '20095' WHERE question_id = '20179' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '30106' WHERE question_id = '30125' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '30106' WHERE question_id = '30125' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '30108' WHERE question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '30108' WHERE question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '30109' WHERE question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '30109' WHERE question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '40011' WHERE question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '40011' WHERE question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '40012' WHERE question_id = '40047' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '40012' WHERE question_id = '40047' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '40013' WHERE question_id = '40050' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '40013' WHERE question_id = '40050' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '40014' WHERE question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '40014' WHERE question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '40015' WHERE question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '40015' WHERE question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation edujpj0n68x5n4ro2tpaxqr0
UPDATE consultation_results SET question_id = '20101' WHERE question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '20101' WHERE question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET question_id = '20102' WHERE question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '20102' WHERE question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET question_id = '20103' WHERE question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '20103' WHERE question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET question_id = '40021' WHERE question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '40021' WHERE question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET question_id = '40022' WHERE question_id = '40063' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '40022' WHERE question_id = '40063' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET question_id = '40023' WHERE question_id = '40062' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '40023' WHERE question_id = '40062' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET question_id = '40024' WHERE question_id = '40064' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '40024' WHERE question_id = '40064' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET question_id = '40025' WHERE question_id = '40065' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '40025' WHERE question_id = '40065' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET question_id = '40026' WHERE question_id = '40066' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '40026' WHERE question_id = '40066' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET question_id = '40027' WHERE question_id = '40067' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '40027' WHERE question_id = '40067' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET question_id = '40028' WHERE question_id = '40070' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '40028' WHERE question_id = '40070' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation esg67bdrkzh0n29j2c858ryj
UPDATE consultation_results SET question_id = '20107' WHERE question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET question_id = '20107' WHERE question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET question_id = '20108' WHERE question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET question_id = '20108' WHERE question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET question_id = '20109' WHERE question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET question_id = '20109' WHERE question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET question_id = '20110' WHERE question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET question_id = '20110' WHERE question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET question_id = '20111' WHERE question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET question_id = '20111' WHERE question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET question_id = '20112' WHERE question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET question_id = '20112' WHERE question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET question_id = '40037' WHERE question_id = '40057' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET question_id = '40037' WHERE question_id = '40057' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';

-- Consultation eykw95ahuc3fb5husp5wnjp7
UPDATE consultation_results SET question_id = '20118' WHERE question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '20118' WHERE question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '20119' WHERE question_id = '20223' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '20119' WHERE question_id = '20223' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '20120' WHERE question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '20120' WHERE question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '20121' WHERE question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '20121' WHERE question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '20122' WHERE question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '20122' WHERE question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '30113' WHERE question_id = '30212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '30113' WHERE question_id = '30212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '30116' WHERE question_id = '30214' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '30116' WHERE question_id = '30214' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '30117' WHERE question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '30117' WHERE question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '30118' WHERE question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '30118' WHERE question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '30119' WHERE question_id = '30227' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '30119' WHERE question_id = '30227' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '30120' WHERE question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '30120' WHERE question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '40043' WHERE question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '40043' WHERE question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '40044' WHERE question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '40044' WHERE question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq
UPDATE consultation_results SET question_id = '20124' WHERE question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '20124' WHERE question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '20125' WHERE question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '20125' WHERE question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '20126' WHERE question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '20126' WHERE question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '20127' WHERE question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '20127' WHERE question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '20128' WHERE question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '20128' WHERE question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '20129' WHERE question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '20129' WHERE question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '20130' WHERE question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '20130' WHERE question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '20131' WHERE question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '20131' WHERE question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '20132' WHERE question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '20132' WHERE question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '30121' WHERE question_id = '30155' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '30121' WHERE question_id = '30155' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '30122' WHERE question_id = '30154' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '30122' WHERE question_id = '30154' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '40045' WHERE question_id = '40053' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '40045' WHERE question_id = '40053' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- ============================================================
-- APRÈS ROLLBACK : invalider le cache Redis
-- redis-cli KEYS "consultation_results_*" | xargs redis-cli DEL
-- ============================================================
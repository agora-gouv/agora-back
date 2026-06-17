-- ============================================================
-- Migration automatique question_id / choice_id
-- Généré le 2026-06-17T18:55:34.097Z
-- Script : generate_migration_sql.js
-- ============================================================

-- Questions mappées     : 224 / 224
-- Questions non mappées : 0
-- Choix mappés          : 1052 / 1139
-- Choix non mappés      : 0
-- Choix ambigus (label dupliqué dans même question) : 0

-- ============================================================
-- ÉTAPE 1 : Mise à jour des question_id
-- ============================================================

-- Consultation dy9l5rzk9r3ocurra7xanvi7
UPDATE consultation_results SET question_id = '20189' WHERE question_id = '20000' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '20189' WHERE question_id = '20000' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '20190' WHERE question_id = '20001' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '20190' WHERE question_id = '20001' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '20191' WHERE question_id = '20002' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '20191' WHERE question_id = '20002' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '30168' WHERE question_id = '30000' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '30168' WHERE question_id = '30000' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '30169' WHERE question_id = '30001' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '30169' WHERE question_id = '30001' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '30170' WHERE question_id = '30002' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '30170' WHERE question_id = '30002' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '30171' WHERE question_id = '30003' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '30171' WHERE question_id = '30003' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '30172' WHERE question_id = '30004' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '30172' WHERE question_id = '30004' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '30173' WHERE question_id = '30005' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '30173' WHERE question_id = '30005' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '30174' WHERE question_id = '30006' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '30174' WHERE question_id = '30006' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '30175' WHERE question_id = '30007' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '30175' WHERE question_id = '30007' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET question_id = '30176' WHERE question_id = '30008' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET question_id = '30176' WHERE question_id = '30008' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2
UPDATE consultation_results SET question_id = '20148' WHERE question_id = '20007' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '20148' WHERE question_id = '20007' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '20149' WHERE question_id = '20008' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '20149' WHERE question_id = '20008' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '20152' WHERE question_id = '20009' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '20152' WHERE question_id = '20009' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '20151' WHERE question_id = '20010' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '20151' WHERE question_id = '20010' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '20150' WHERE question_id = '20011' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '20150' WHERE question_id = '20011' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '20153' WHERE question_id = '20012' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '20153' WHERE question_id = '20012' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '20158' WHERE question_id = '20013' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '20158' WHERE question_id = '20013' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '30134' WHERE question_id = '30012' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '30134' WHERE question_id = '30012' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '30135' WHERE question_id = '30013' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '30135' WHERE question_id = '30013' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '30136' WHERE question_id = '30014' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '30136' WHERE question_id = '30014' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '30138' WHERE question_id = '30015' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '30138' WHERE question_id = '30015' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '30139' WHERE question_id = '30016' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '30139' WHERE question_id = '30016' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '30137' WHERE question_id = '30017' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '30137' WHERE question_id = '30017' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET question_id = '40051' WHERE question_id = '40000' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET question_id = '40051' WHERE question_id = '40000' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation pwr61j5qhpm6ekrf3liuw5bc
UPDATE consultation_results SET question_id = '20145' WHERE question_id = '20003' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET question_id = '20145' WHERE question_id = '20003' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET question_id = '20144' WHERE question_id = '20004' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET question_id = '20144' WHERE question_id = '20004' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET question_id = '20146' WHERE question_id = '20005' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET question_id = '20146' WHERE question_id = '20005' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET question_id = '20147' WHERE question_id = '20006' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET question_id = '20147' WHERE question_id = '20006' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET question_id = '30131' WHERE question_id = '30009' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET question_id = '30131' WHERE question_id = '30009' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET question_id = '30132' WHERE question_id = '30010' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET question_id = '30132' WHERE question_id = '30010' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET question_id = '30133' WHERE question_id = '30011' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET question_id = '30133' WHERE question_id = '30011' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';

-- Consultation vciluo7jt2hiavx7wuqbfwh1
UPDATE consultation_results SET question_id = '20160' WHERE question_id = '20014' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '20160' WHERE question_id = '20014' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '20161' WHERE question_id = '20015' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '20161' WHERE question_id = '20015' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '20162' WHERE question_id = '20016' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '20162' WHERE question_id = '20016' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '20172' WHERE question_id = '20017' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '20172' WHERE question_id = '20017' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '20163' WHERE question_id = '20018' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '20163' WHERE question_id = '20018' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '20173' WHERE question_id = '20019' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '20173' WHERE question_id = '20019' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '30142' WHERE question_id = '30018' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '30142' WHERE question_id = '30018' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '30143' WHERE question_id = '30019' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '30143' WHERE question_id = '30019' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '30144' WHERE question_id = '30020' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '30144' WHERE question_id = '30020' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '30145' WHERE question_id = '30021' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '30145' WHERE question_id = '30021' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '30150' WHERE question_id = '30022' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '30150' WHERE question_id = '30022' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '30151' WHERE question_id = '30023' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '30151' WHERE question_id = '30023' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '30153' WHERE question_id = '30024' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '30153' WHERE question_id = '30024' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '30152' WHERE question_id = '30025' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '30152' WHERE question_id = '30025' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '40052' WHERE question_id = '40001' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '40052' WHERE question_id = '40001' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET question_id = '40055' WHERE question_id = '40002' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET question_id = '40055' WHERE question_id = '40002' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo
UPDATE consultation_results SET question_id = '20133' WHERE question_id = '20020' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '20133' WHERE question_id = '20020' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '20134' WHERE question_id = '20021' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '20134' WHERE question_id = '20021' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '20135' WHERE question_id = '20022' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '20135' WHERE question_id = '20022' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '20139' WHERE question_id = '20023' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '20139' WHERE question_id = '20023' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '20140' WHERE question_id = '20024' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '20140' WHERE question_id = '20024' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '20141' WHERE question_id = '20025' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '20141' WHERE question_id = '20025' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '20142' WHERE question_id = '20026' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '20142' WHERE question_id = '20026' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '30123' WHERE question_id = '30026' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '30123' WHERE question_id = '30026' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '30124' WHERE question_id = '30027' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '30124' WHERE question_id = '30027' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '30127' WHERE question_id = '30028' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '30127' WHERE question_id = '30028' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '30129' WHERE question_id = '30029' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '30129' WHERE question_id = '30029' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '30128' WHERE question_id = '30030' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '30128' WHERE question_id = '30030' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET question_id = '30130' WHERE question_id = '30031' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET question_id = '30130' WHERE question_id = '30031' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation kmbc0ie5zsx94dejoiv1wcnv
UPDATE consultation_results SET question_id = '20165' WHERE question_id = '20028' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET question_id = '20165' WHERE question_id = '20028' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET question_id = '20166' WHERE question_id = '20029' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET question_id = '20166' WHERE question_id = '20029' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET question_id = '20167' WHERE question_id = '20030' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET question_id = '20167' WHERE question_id = '20030' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET question_id = '20169' WHERE question_id = '20031' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET question_id = '20169' WHERE question_id = '20031' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET question_id = '20168' WHERE question_id = '20032' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET question_id = '20168' WHERE question_id = '20032' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET question_id = '30146' WHERE question_id = '30032' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET question_id = '30146' WHERE question_id = '30032' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET question_id = '30148' WHERE question_id = '30033' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET question_id = '30148' WHERE question_id = '30033' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET question_id = '30147' WHERE question_id = '30034' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET question_id = '30147' WHERE question_id = '30034' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET question_id = '30149' WHERE question_id = '30035' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET question_id = '30149' WHERE question_id = '30035' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET question_id = '40054' WHERE question_id = '40003' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET question_id = '40054' WHERE question_id = '40003' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation w5d0rrhdlqby9shpt4wdk5oj
UPDATE consultation_results SET question_id = '20154' WHERE question_id = '20033' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET question_id = '20154' WHERE question_id = '20033' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET question_id = '20155' WHERE question_id = '20034' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET question_id = '20155' WHERE question_id = '20034' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET question_id = '20157' WHERE question_id = '20035' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET question_id = '20157' WHERE question_id = '20035' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET question_id = '20156' WHERE question_id = '20036' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET question_id = '20156' WHERE question_id = '20036' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET question_id = '30140' WHERE question_id = '30036' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET question_id = '30140' WHERE question_id = '30036' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET question_id = '30141' WHERE question_id = '30037' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET question_id = '30141' WHERE question_id = '30037' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';

-- Consultation wv0ul09acpw4f38v6s4uudqn
UPDATE consultation_results SET question_id = '20196' WHERE question_id = '20037' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '20196' WHERE question_id = '20037' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '20197' WHERE question_id = '20038' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '20197' WHERE question_id = '20038' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '20209' WHERE question_id = '20039' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '20209' WHERE question_id = '20039' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '20210' WHERE question_id = '20040' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '20210' WHERE question_id = '20040' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '20211' WHERE question_id = '20041' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '20211' WHERE question_id = '20041' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30166' WHERE question_id = '30038' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30166' WHERE question_id = '30038' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30167' WHERE question_id = '30039' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30167' WHERE question_id = '30039' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30180' WHERE question_id = '30040' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30180' WHERE question_id = '30040' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30179' WHERE question_id = '30041' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30179' WHERE question_id = '30041' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30178' WHERE question_id = '30042' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30178' WHERE question_id = '30042' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30182' WHERE question_id = '30043' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30182' WHERE question_id = '30043' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30181' WHERE question_id = '30044' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30181' WHERE question_id = '30044' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30183' WHERE question_id = '30045' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30183' WHERE question_id = '30045' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30186' WHERE question_id = '30046' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30186' WHERE question_id = '30046' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30185' WHERE question_id = '30047' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30185' WHERE question_id = '30047' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30188' WHERE question_id = '30048' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30188' WHERE question_id = '30048' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30184' WHERE question_id = '30049' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30184' WHERE question_id = '30049' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30187' WHERE question_id = '30050' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30187' WHERE question_id = '30050' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30189' WHERE question_id = '30051' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30189' WHERE question_id = '30051' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30190' WHERE question_id = '30052' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30190' WHERE question_id = '30052' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30191' WHERE question_id = '30053' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30191' WHERE question_id = '30053' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30193' WHERE question_id = '30054' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30193' WHERE question_id = '30054' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30192' WHERE question_id = '30055' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30192' WHERE question_id = '30055' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30194' WHERE question_id = '30056' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30194' WHERE question_id = '30056' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30197' WHERE question_id = '30057' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30197' WHERE question_id = '30057' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30195' WHERE question_id = '30058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30195' WHERE question_id = '30058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30198' WHERE question_id = '30059' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30198' WHERE question_id = '30059' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30196' WHERE question_id = '30060' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30196' WHERE question_id = '30060' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30199' WHERE question_id = '30061' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30199' WHERE question_id = '30061' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30204' WHERE question_id = '30062' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30204' WHERE question_id = '30062' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30201' WHERE question_id = '30063' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30201' WHERE question_id = '30063' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30200' WHERE question_id = '30064' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30200' WHERE question_id = '30064' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30208' WHERE question_id = '30065' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30208' WHERE question_id = '30065' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30205' WHERE question_id = '30066' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30205' WHERE question_id = '30066' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30206' WHERE question_id = '30067' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30206' WHERE question_id = '30067' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30207' WHERE question_id = '30068' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30207' WHERE question_id = '30068' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30209' WHERE question_id = '30069' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30209' WHERE question_id = '30069' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30210' WHERE question_id = '30070' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30210' WHERE question_id = '30070' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '30211' WHERE question_id = '30071' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '30211' WHERE question_id = '30071' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '40058' WHERE question_id = '40004' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '40058' WHERE question_id = '40004' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '40059' WHERE question_id = '40005' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '40059' WHERE question_id = '40005' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET question_id = '40060' WHERE question_id = '40006' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET question_id = '40060' WHERE question_id = '40006' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation g8w8ivfieyvs3ajinnbk4015
UPDATE consultation_results SET question_id = '20180' WHERE question_id = '20042' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '20180' WHERE question_id = '20042' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '20181' WHERE question_id = '20043' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '20181' WHERE question_id = '20043' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '20182' WHERE question_id = '20044' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '20182' WHERE question_id = '20044' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '20183' WHERE question_id = '20045' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '20183' WHERE question_id = '20045' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '30160' WHERE question_id = '30072' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '30160' WHERE question_id = '30072' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '30158' WHERE question_id = '30073' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '30158' WHERE question_id = '30073' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '30157' WHERE question_id = '30074' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '30157' WHERE question_id = '30074' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '30159' WHERE question_id = '30075' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '30159' WHERE question_id = '30075' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '30161' WHERE question_id = '30076' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '30161' WHERE question_id = '30076' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '30162' WHERE question_id = '30077' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '30162' WHERE question_id = '30077' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '30164' WHERE question_id = '30078' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '30164' WHERE question_id = '30078' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '30165' WHERE question_id = '30079' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '30165' WHERE question_id = '30079' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET question_id = '30163' WHERE question_id = '30080' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET question_id = '30163' WHERE question_id = '30080' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation vkp77x0ognt41xyjicz9wwba
UPDATE consultation_results SET question_id = '20199' WHERE question_id = '20046' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET question_id = '20199' WHERE question_id = '20046' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET question_id = '20198' WHERE question_id = '20047' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET question_id = '20198' WHERE question_id = '20047' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET question_id = '20200' WHERE question_id = '20048' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET question_id = '20200' WHERE question_id = '20048' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET question_id = '20201' WHERE question_id = '20049' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET question_id = '20201' WHERE question_id = '20049' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET question_id = '30202' WHERE question_id = '30081' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET question_id = '30202' WHERE question_id = '30081' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET question_id = '30203' WHERE question_id = '30082' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET question_id = '30203' WHERE question_id = '30082' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';

-- Consultation rn3ib6tpe4yv3l7ofolyqh7d
UPDATE consultation_results SET question_id = '20213' WHERE question_id = '20050' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET question_id = '20213' WHERE question_id = '20050' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET question_id = '20214' WHERE question_id = '20051' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET question_id = '20214' WHERE question_id = '20051' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET question_id = '20215' WHERE question_id = '20052' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET question_id = '20215' WHERE question_id = '20052' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET question_id = '20216' WHERE question_id = '20053' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET question_id = '20216' WHERE question_id = '20053' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET question_id = '30215' WHERE question_id = '30083' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET question_id = '30215' WHERE question_id = '30083' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET question_id = '30217' WHERE question_id = '30084' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET question_id = '30217' WHERE question_id = '30084' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET question_id = '30216' WHERE question_id = '30085' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET question_id = '30216' WHERE question_id = '30085' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';

-- Consultation ei80m02y5koialuq4e30n4q6
UPDATE consultation_results SET question_id = '20217' WHERE question_id = '20054' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '20217' WHERE question_id = '20054' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '20219' WHERE question_id = '20055' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '20219' WHERE question_id = '20055' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '20218' WHERE question_id = '20056' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '20218' WHERE question_id = '20056' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '20221' WHERE question_id = '20058' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '20221' WHERE question_id = '20058' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '20220' WHERE question_id = '20059' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '20220' WHERE question_id = '20059' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '20222' WHERE question_id = '20060' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '20222' WHERE question_id = '20060' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '30223' WHERE question_id = '30087' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '30223' WHERE question_id = '30087' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '30222' WHERE question_id = '30088' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '30222' WHERE question_id = '30088' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '30221' WHERE question_id = '30089' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '30221' WHERE question_id = '30089' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '30224' WHERE question_id = '30090' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '30224' WHERE question_id = '30090' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '30218' WHERE question_id = '30091' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '30218' WHERE question_id = '30091' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '30219' WHERE question_id = '30092' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '30219' WHERE question_id = '30092' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET question_id = '30220' WHERE question_id = '30093' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET question_id = '30220' WHERE question_id = '30093' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation jf84upj9qutugwkce773m8dt
UPDATE consultation_results SET question_id = '20187' WHERE question_id = '20084' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET question_id = '20187' WHERE question_id = '20084' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET question_id = '20185' WHERE question_id = '20085' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET question_id = '20185' WHERE question_id = '20085' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET question_id = '20192' WHERE question_id = '20086' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET question_id = '20192' WHERE question_id = '20086' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET question_id = '20193' WHERE question_id = '20087' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET question_id = '20193' WHERE question_id = '20087' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET question_id = '20188' WHERE question_id = '20088' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET question_id = '20188' WHERE question_id = '20088' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET question_id = '20184' WHERE question_id = '20089' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET question_id = '20184' WHERE question_id = '20089' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET question_id = '20194' WHERE question_id = '20090' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET question_id = '20194' WHERE question_id = '20090' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET question_id = '30177' WHERE question_id = '30105' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET question_id = '30177' WHERE question_id = '30105' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET question_id = '40056' WHERE question_id = '40010' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET question_id = '40056' WHERE question_id = '40010' AND consultation_id = 'jf84upj9qutugwkce773m8dt';

-- Consultation hxec8jg448kdtr5uy4jhxt8y
UPDATE consultation_results SET question_id = '20137' WHERE question_id = '20091' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '20137' WHERE question_id = '20091' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '20138' WHERE question_id = '20092' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '20138' WHERE question_id = '20092' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '20178' WHERE question_id = '20093' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '20178' WHERE question_id = '20093' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '20143' WHERE question_id = '20094' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '20143' WHERE question_id = '20094' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '20179' WHERE question_id = '20095' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '20179' WHERE question_id = '20095' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '30125' WHERE question_id = '30106' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '30125' WHERE question_id = '30106' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '30126' WHERE question_id = '30108' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '30126' WHERE question_id = '30108' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '30156' WHERE question_id = '30109' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '30156' WHERE question_id = '30109' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '40046' WHERE question_id = '40011' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '40046' WHERE question_id = '40011' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '40047' WHERE question_id = '40012' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '40047' WHERE question_id = '40012' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '40050' WHERE question_id = '40013' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '40050' WHERE question_id = '40013' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '40049' WHERE question_id = '40014' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '40049' WHERE question_id = '40014' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET question_id = '40048' WHERE question_id = '40015' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET question_id = '40048' WHERE question_id = '40015' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation edujpj0n68x5n4ro2tpaxqr0
UPDATE consultation_results SET question_id = '20203' WHERE question_id = '20101' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '20203' WHERE question_id = '20101' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET question_id = '20204' WHERE question_id = '20102' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '20204' WHERE question_id = '20102' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET question_id = '20205' WHERE question_id = '20103' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '20205' WHERE question_id = '20103' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET question_id = '40061' WHERE question_id = '40021' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '40061' WHERE question_id = '40021' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET question_id = '40063' WHERE question_id = '40022' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '40063' WHERE question_id = '40022' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET question_id = '40062' WHERE question_id = '40023' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '40062' WHERE question_id = '40023' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET question_id = '40064' WHERE question_id = '40024' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '40064' WHERE question_id = '40024' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET question_id = '40065' WHERE question_id = '40025' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '40065' WHERE question_id = '40025' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET question_id = '40066' WHERE question_id = '40026' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '40066' WHERE question_id = '40026' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET question_id = '40067' WHERE question_id = '40027' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '40067' WHERE question_id = '40027' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET question_id = '40070' WHERE question_id = '40028' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET question_id = '40070' WHERE question_id = '40028' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation esg67bdrkzh0n29j2c858ryj
UPDATE consultation_results SET question_id = '20186' WHERE question_id = '20107' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET question_id = '20186' WHERE question_id = '20107' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET question_id = '20202' WHERE question_id = '20108' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET question_id = '20202' WHERE question_id = '20108' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET question_id = '20195' WHERE question_id = '20109' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET question_id = '20195' WHERE question_id = '20109' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET question_id = '20206' WHERE question_id = '20110' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET question_id = '20206' WHERE question_id = '20110' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET question_id = '20208' WHERE question_id = '20111' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET question_id = '20208' WHERE question_id = '20111' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET question_id = '20207' WHERE question_id = '20112' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET question_id = '20207' WHERE question_id = '20112' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET question_id = '40057' WHERE question_id = '40037' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET question_id = '40057' WHERE question_id = '40037' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';

-- Consultation eykw95ahuc3fb5husp5wnjp7
UPDATE consultation_results SET question_id = '20212' WHERE question_id = '20118' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '20212' WHERE question_id = '20118' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '20223' WHERE question_id = '20119' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '20223' WHERE question_id = '20119' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '20224' WHERE question_id = '20120' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '20224' WHERE question_id = '20120' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '20226' WHERE question_id = '20121' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '20226' WHERE question_id = '20121' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '20225' WHERE question_id = '20122' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '20225' WHERE question_id = '20122' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '30212' WHERE question_id = '30113' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '30212' WHERE question_id = '30113' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '30214' WHERE question_id = '30116' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '30214' WHERE question_id = '30116' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '30225' WHERE question_id = '30117' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '30225' WHERE question_id = '30117' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '30213' WHERE question_id = '30118' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '30213' WHERE question_id = '30118' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '30227' WHERE question_id = '30119' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '30227' WHERE question_id = '30119' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '30226' WHERE question_id = '30120' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '30226' WHERE question_id = '30120' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '40068' WHERE question_id = '40043' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '40068' WHERE question_id = '40043' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET question_id = '40069' WHERE question_id = '40044' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET question_id = '40069' WHERE question_id = '40044' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq
UPDATE consultation_results SET question_id = '20136' WHERE question_id = '20124' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '20136' WHERE question_id = '20124' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '20159' WHERE question_id = '20125' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '20159' WHERE question_id = '20125' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '20164' WHERE question_id = '20126' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '20164' WHERE question_id = '20126' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '20170' WHERE question_id = '20127' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '20170' WHERE question_id = '20127' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '20171' WHERE question_id = '20128' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '20171' WHERE question_id = '20128' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '20174' WHERE question_id = '20129' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '20174' WHERE question_id = '20129' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '20175' WHERE question_id = '20130' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '20175' WHERE question_id = '20130' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '20176' WHERE question_id = '20131' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '20176' WHERE question_id = '20131' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '20177' WHERE question_id = '20132' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '20177' WHERE question_id = '20132' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '30155' WHERE question_id = '30121' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '30155' WHERE question_id = '30121' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '30154' WHERE question_id = '30122' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '30154' WHERE question_id = '30122' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET question_id = '40053' WHERE question_id = '40045' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET question_id = '40053' WHERE question_id = '40045' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- ============================================================
-- ÉTAPE 2 : Mise à jour des choice_id
-- ============================================================

-- Consultation kmbc0ie5zsx94dejoiv1wcnv / question 40054
UPDATE consultation_results SET choice_id = '196' WHERE choice_id = '13' AND question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '196' WHERE choice_id = '13' AND question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '221' WHERE choice_id = '14' AND question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '221' WHERE choice_id = '14' AND question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '235' WHERE choice_id = '15' AND question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '235' WHERE choice_id = '15' AND question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '245' WHERE choice_id = '16' AND question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '245' WHERE choice_id = '16' AND question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '171' WHERE choice_id = '12' AND question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '171' WHERE choice_id = '12' AND question_id = '40054' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation dy9l5rzk9r3ocurra7xanvi7 / question 20190
UPDATE consultation_results SET choice_id = '1538' WHERE choice_id = '31' AND question_id = '20190' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '1538' WHERE choice_id = '31' AND question_id = '20190' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';

-- Consultation jf84upj9qutugwkce773m8dt / question 40056
UPDATE consultation_results SET choice_id = '173' WHERE choice_id = '32' AND question_id = '40056' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '173' WHERE choice_id = '32' AND question_id = '40056' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '198' WHERE choice_id = '33' AND question_id = '40056' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '198' WHERE choice_id = '33' AND question_id = '40056' AND consultation_id = 'jf84upj9qutugwkce773m8dt';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 40046
UPDATE consultation_results SET choice_id = '163' WHERE choice_id = '34' AND question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '163' WHERE choice_id = '34' AND question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '188' WHERE choice_id = '35' AND question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '188' WHERE choice_id = '35' AND question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '213' WHERE choice_id = '36' AND question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '213' WHERE choice_id = '36' AND question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '228' WHERE choice_id = '37' AND question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '228' WHERE choice_id = '37' AND question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '241' WHERE choice_id = '55' AND question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '241' WHERE choice_id = '55' AND question_id = '40046' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 40050
UPDATE consultation_results SET choice_id = '167' WHERE choice_id = '47' AND question_id = '40050' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '167' WHERE choice_id = '47' AND question_id = '40050' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '217' WHERE choice_id = '48' AND question_id = '40050' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '217' WHERE choice_id = '48' AND question_id = '40050' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '192' WHERE choice_id = '45' AND question_id = '40050' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '192' WHERE choice_id = '45' AND question_id = '40050' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 40049
UPDATE consultation_results SET choice_id = '166' WHERE choice_id = '49' AND question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '166' WHERE choice_id = '49' AND question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '216' WHERE choice_id = '42' AND question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '216' WHERE choice_id = '42' AND question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '191' WHERE choice_id = '43' AND question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '191' WHERE choice_id = '43' AND question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '231' WHERE choice_id = '44' AND question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '231' WHERE choice_id = '44' AND question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '243' WHERE choice_id = '46' AND question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '243' WHERE choice_id = '46' AND question_id = '40049' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 40048
UPDATE consultation_results SET choice_id = '165' WHERE choice_id = '50' AND question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '165' WHERE choice_id = '50' AND question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '215' WHERE choice_id = '52' AND question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '215' WHERE choice_id = '52' AND question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '230' WHERE choice_id = '53' AND question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '230' WHERE choice_id = '53' AND question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '242' WHERE choice_id = '54' AND question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '242' WHERE choice_id = '54' AND question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '190' WHERE choice_id = '51' AND question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '190' WHERE choice_id = '51' AND question_id = '40048' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 40066
UPDATE consultation_results SET choice_id = '183' WHERE choice_id = '93' AND question_id = '40066' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '183' WHERE choice_id = '93' AND question_id = '40066' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '208' WHERE choice_id = '94' AND question_id = '40066' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '208' WHERE choice_id = '94' AND question_id = '40066' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 40067
UPDATE consultation_results SET choice_id = '184' WHERE choice_id = '95' AND question_id = '40067' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '184' WHERE choice_id = '95' AND question_id = '40067' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '209' WHERE choice_id = '96' AND question_id = '40067' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '209' WHERE choice_id = '96' AND question_id = '40067' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '225' WHERE choice_id = '97' AND question_id = '40067' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '225' WHERE choice_id = '97' AND question_id = '40067' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '238' WHERE choice_id = '99' AND question_id = '40067' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '238' WHERE choice_id = '99' AND question_id = '40067' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 40070
UPDATE consultation_results SET choice_id = '187' WHERE choice_id = '98' AND question_id = '40070' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '187' WHERE choice_id = '98' AND question_id = '40070' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '212' WHERE choice_id = '100' AND question_id = '40070' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '212' WHERE choice_id = '100' AND question_id = '40070' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 40061
UPDATE consultation_results SET choice_id = '250' WHERE choice_id = '83' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '250' WHERE choice_id = '83' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '251' WHERE choice_id = '84' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '251' WHERE choice_id = '84' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '178' WHERE choice_id = '78' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '178' WHERE choice_id = '78' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '203' WHERE choice_id = '79' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '203' WHERE choice_id = '79' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '224' WHERE choice_id = '80' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '224' WHERE choice_id = '80' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '237' WHERE choice_id = '81' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '237' WHERE choice_id = '81' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '247' WHERE choice_id = '82' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '247' WHERE choice_id = '82' AND question_id = '40061' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 40063
UPDATE consultation_results SET choice_id = '180' WHERE choice_id = '87' AND question_id = '40063' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '180' WHERE choice_id = '87' AND question_id = '40063' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '205' WHERE choice_id = '88' AND question_id = '40063' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '205' WHERE choice_id = '88' AND question_id = '40063' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 40064
UPDATE consultation_results SET choice_id = '181' WHERE choice_id = '89' AND question_id = '40064' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '181' WHERE choice_id = '89' AND question_id = '40064' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '206' WHERE choice_id = '90' AND question_id = '40064' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '206' WHERE choice_id = '90' AND question_id = '40064' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 40065
UPDATE consultation_results SET choice_id = '182' WHERE choice_id = '91' AND question_id = '40065' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '182' WHERE choice_id = '91' AND question_id = '40065' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '207' WHERE choice_id = '92' AND question_id = '40065' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '207' WHERE choice_id = '92' AND question_id = '40065' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation pwr61j5qhpm6ekrf3liuw5bc / question 20146
UPDATE consultation_results SET choice_id = '1851' WHERE choice_id = '101' AND question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '1851' WHERE choice_id = '101' AND question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '1652' WHERE choice_id = '102' AND question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '1652' WHERE choice_id = '102' AND question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '2019' WHERE choice_id = '103' AND question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '2019' WHERE choice_id = '103' AND question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '2158' WHERE choice_id = '104' AND question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '2158' WHERE choice_id = '104' AND question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '2263' WHERE choice_id = '105' AND question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '2263' WHERE choice_id = '105' AND question_id = '20146' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';

-- Consultation pwr61j5qhpm6ekrf3liuw5bc / question 20147
UPDATE consultation_results SET choice_id = '1653' WHERE choice_id = '106' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '1653' WHERE choice_id = '106' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '1454' WHERE choice_id = '107' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '1454' WHERE choice_id = '107' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '1852' WHERE choice_id = '108' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '1852' WHERE choice_id = '108' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '2020' WHERE choice_id = '109' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '2020' WHERE choice_id = '109' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '2159' WHERE choice_id = '110' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '2159' WHERE choice_id = '110' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '2264' WHERE choice_id = '111' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '2264' WHERE choice_id = '111' AND question_id = '20147' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';

-- Consultation esg67bdrkzh0n29j2c858ryj / question 40057
UPDATE consultation_results SET choice_id = '222' WHERE choice_id = '125' AND question_id = '40057' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '222' WHERE choice_id = '125' AND question_id = '40057' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '199' WHERE choice_id = '126' AND question_id = '40057' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '199' WHERE choice_id = '126' AND question_id = '40057' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '174' WHERE choice_id = '124' AND question_id = '40057' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '174' WHERE choice_id = '124' AND question_id = '40057' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 20148
UPDATE consultation_results SET choice_id = '2023' WHERE choice_id = '127' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2023' WHERE choice_id = '127' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2267' WHERE choice_id = '128' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2267' WHERE choice_id = '128' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2340' WHERE choice_id = '129' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2340' WHERE choice_id = '129' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2394' WHERE choice_id = '130' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2394' WHERE choice_id = '130' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1855' WHERE choice_id = '131' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1855' WHERE choice_id = '131' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2423' WHERE choice_id = '132' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2423' WHERE choice_id = '132' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2162' WHERE choice_id = '133' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2162' WHERE choice_id = '133' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2441' WHERE choice_id = '134' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2441' WHERE choice_id = '134' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2451' WHERE choice_id = '135' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2451' WHERE choice_id = '135' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2456' WHERE choice_id = '136' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2456' WHERE choice_id = '136' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2461' WHERE choice_id = '137' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2461' WHERE choice_id = '137' AND question_id = '20148' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 20149
UPDATE consultation_results SET choice_id = '1459' WHERE choice_id = '142' AND question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1459' WHERE choice_id = '142' AND question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2025' WHERE choice_id = '143' AND question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2025' WHERE choice_id = '143' AND question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1857' WHERE choice_id = '144' AND question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1857' WHERE choice_id = '144' AND question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1658' WHERE choice_id = '145' AND question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1658' WHERE choice_id = '145' AND question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2163' WHERE choice_id = '146' AND question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2163' WHERE choice_id = '146' AND question_id = '20149' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 20152
UPDATE consultation_results SET choice_id = '1661' WHERE choice_id = '163' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1661' WHERE choice_id = '163' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2270' WHERE choice_id = '165' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2270' WHERE choice_id = '165' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2343' WHERE choice_id = '166' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2343' WHERE choice_id = '166' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1860' WHERE choice_id = '167' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1860' WHERE choice_id = '167' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2166' WHERE choice_id = '168' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2166' WHERE choice_id = '168' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2028' WHERE choice_id = '169' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2028' WHERE choice_id = '169' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1462' WHERE choice_id = '170' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1462' WHERE choice_id = '170' AND question_id = '20152' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 40069
UPDATE consultation_results SET choice_id = '227' WHERE choice_id = '156' AND question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '227' WHERE choice_id = '156' AND question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '186' WHERE choice_id = '157' AND question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '186' WHERE choice_id = '157' AND question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '211' WHERE choice_id = '158' AND question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '211' WHERE choice_id = '158' AND question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '240' WHERE choice_id = '154' AND question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '240' WHERE choice_id = '154' AND question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '249' WHERE choice_id = '155' AND question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '249' WHERE choice_id = '155' AND question_id = '40069' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 40053
UPDATE consultation_results SET choice_id = '234' WHERE choice_id = '159' AND question_id = '40053' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '234' WHERE choice_id = '159' AND question_id = '40053' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '170' WHERE choice_id = '160' AND question_id = '40053' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '170' WHERE choice_id = '160' AND question_id = '40053' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '195' WHERE choice_id = '161' AND question_id = '40053' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '195' WHERE choice_id = '161' AND question_id = '40053' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '220' WHERE choice_id = '162' AND question_id = '40053' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '220' WHERE choice_id = '162' AND question_id = '40053' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 20151
UPDATE consultation_results SET choice_id = '2396' WHERE choice_id = '164' AND question_id = '20151' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2396' WHERE choice_id = '164' AND question_id = '20151' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 20150
UPDATE consultation_results SET choice_id = '2164' WHERE choice_id = '147' AND question_id = '20150' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2164' WHERE choice_id = '147' AND question_id = '20150' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1858' WHERE choice_id = '148' AND question_id = '20150' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1858' WHERE choice_id = '148' AND question_id = '20150' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 40068
UPDATE consultation_results SET choice_id = '185' WHERE choice_id = '149' AND question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '185' WHERE choice_id = '149' AND question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '239' WHERE choice_id = '150' AND question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '239' WHERE choice_id = '150' AND question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '210' WHERE choice_id = '151' AND question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '210' WHERE choice_id = '151' AND question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '248' WHERE choice_id = '152' AND question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '248' WHERE choice_id = '152' AND question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '226' WHERE choice_id = '153' AND question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '226' WHERE choice_id = '153' AND question_id = '40068' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 20153
UPDATE consultation_results SET choice_id = '1465' WHERE choice_id = '177' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1465' WHERE choice_id = '177' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1664' WHERE choice_id = '178' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1664' WHERE choice_id = '178' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1863' WHERE choice_id = '179' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1863' WHERE choice_id = '179' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2029' WHERE choice_id = '180' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2029' WHERE choice_id = '180' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2167' WHERE choice_id = '181' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2167' WHERE choice_id = '181' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2271' WHERE choice_id = '183' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2271' WHERE choice_id = '183' AND question_id = '20153' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 20158
UPDATE consultation_results SET choice_id = '1672' WHERE choice_id = '185' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1672' WHERE choice_id = '185' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1870' WHERE choice_id = '186' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1870' WHERE choice_id = '186' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2036' WHERE choice_id = '187' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2036' WHERE choice_id = '187' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2173' WHERE choice_id = '188' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2173' WHERE choice_id = '188' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2276' WHERE choice_id = '189' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2276' WHERE choice_id = '189' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2348' WHERE choice_id = '190' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2348' WHERE choice_id = '190' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2401' WHERE choice_id = '191' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2401' WHERE choice_id = '191' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2428' WHERE choice_id = '192' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2428' WHERE choice_id = '192' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1473' WHERE choice_id = '833' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1473' WHERE choice_id = '833' AND question_id = '20158' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 20160
UPDATE consultation_results SET choice_id = '1475' WHERE choice_id = '193' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1475' WHERE choice_id = '193' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1674' WHERE choice_id = '194' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1674' WHERE choice_id = '194' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2278' WHERE choice_id = '195' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2278' WHERE choice_id = '195' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2038' WHERE choice_id = '196' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2038' WHERE choice_id = '196' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1872' WHERE choice_id = '197' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1872' WHERE choice_id = '197' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2175' WHERE choice_id = '198' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2175' WHERE choice_id = '198' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2350' WHERE choice_id = '199' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2350' WHERE choice_id = '199' AND question_id = '20160' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 20161
UPDATE consultation_results SET choice_id = '1676' WHERE choice_id = '204' AND question_id = '20161' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1676' WHERE choice_id = '204' AND question_id = '20161' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1874' WHERE choice_id = '205' AND question_id = '20161' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1874' WHERE choice_id = '205' AND question_id = '20161' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2040' WHERE choice_id = '207' AND question_id = '20161' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2040' WHERE choice_id = '207' AND question_id = '20161' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1477' WHERE choice_id = '830' AND question_id = '20161' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1477' WHERE choice_id = '830' AND question_id = '20161' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 20162
UPDATE consultation_results SET choice_id = '1677' WHERE choice_id = '206' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1677' WHERE choice_id = '206' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1875' WHERE choice_id = '208' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1875' WHERE choice_id = '208' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2041' WHERE choice_id = '209' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2041' WHERE choice_id = '209' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2176' WHERE choice_id = '210' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2176' WHERE choice_id = '210' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2279' WHERE choice_id = '211' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2279' WHERE choice_id = '211' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2351' WHERE choice_id = '212' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2351' WHERE choice_id = '212' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2402' WHERE choice_id = '213' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2402' WHERE choice_id = '213' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1478' WHERE choice_id = '831' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1478' WHERE choice_id = '831' AND question_id = '20162' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 20172
UPDATE consultation_results SET choice_id = '1495' WHERE choice_id = '229' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1495' WHERE choice_id = '229' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2055' WHERE choice_id = '230' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2055' WHERE choice_id = '230' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1694' WHERE choice_id = '231' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1694' WHERE choice_id = '231' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1891' WHERE choice_id = '232' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1891' WHERE choice_id = '232' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2188' WHERE choice_id = '233' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2188' WHERE choice_id = '233' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2360' WHERE choice_id = '234' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2360' WHERE choice_id = '234' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2290' WHERE choice_id = '235' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2290' WHERE choice_id = '235' AND question_id = '20172' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 20163
UPDATE consultation_results SET choice_id = '1680' WHERE choice_id = '220' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1680' WHERE choice_id = '220' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1878' WHERE choice_id = '221' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1878' WHERE choice_id = '221' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2280' WHERE choice_id = '222' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2280' WHERE choice_id = '222' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2177' WHERE choice_id = '223' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2177' WHERE choice_id = '223' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2042' WHERE choice_id = '224' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2042' WHERE choice_id = '224' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2352' WHERE choice_id = '225' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2352' WHERE choice_id = '225' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2403' WHERE choice_id = '226' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2403' WHERE choice_id = '226' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1481' WHERE choice_id = '832' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1481' WHERE choice_id = '832' AND question_id = '20163' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 20173
UPDATE consultation_results SET choice_id = '1892' WHERE choice_id = '236' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1892' WHERE choice_id = '236' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1695' WHERE choice_id = '237' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1695' WHERE choice_id = '237' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1496' WHERE choice_id = '238' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1496' WHERE choice_id = '238' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2291' WHERE choice_id = '239' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2291' WHERE choice_id = '239' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2189' WHERE choice_id = '240' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2189' WHERE choice_id = '240' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2056' WHERE choice_id = '241' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2056' WHERE choice_id = '241' AND question_id = '20173' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 20133
UPDATE consultation_results SET choice_id = '1429' WHERE choice_id = '252' AND question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1429' WHERE choice_id = '252' AND question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1628' WHERE choice_id = '253' AND question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1628' WHERE choice_id = '253' AND question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1827' WHERE choice_id = '254' AND question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1827' WHERE choice_id = '254' AND question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2001' WHERE choice_id = '255' AND question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2001' WHERE choice_id = '255' AND question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2142' WHERE choice_id = '256' AND question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2142' WHERE choice_id = '256' AND question_id = '20133' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 20134
UPDATE consultation_results SET choice_id = '1629' WHERE choice_id = '257' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1629' WHERE choice_id = '257' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1828' WHERE choice_id = '258' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1828' WHERE choice_id = '258' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2002' WHERE choice_id = '259' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2002' WHERE choice_id = '259' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2143' WHERE choice_id = '260' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2143' WHERE choice_id = '260' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2251' WHERE choice_id = '261' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2251' WHERE choice_id = '261' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2329' WHERE choice_id = '262' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2329' WHERE choice_id = '262' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1430' WHERE choice_id = '263' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1430' WHERE choice_id = '263' AND question_id = '20134' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 20135
UPDATE consultation_results SET choice_id = '1632' WHERE choice_id = '274' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1632' WHERE choice_id = '274' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1831' WHERE choice_id = '275' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1831' WHERE choice_id = '275' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2253' WHERE choice_id = '276' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2253' WHERE choice_id = '276' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2004' WHERE choice_id = '277' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2004' WHERE choice_id = '277' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2145' WHERE choice_id = '278' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2145' WHERE choice_id = '278' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2331' WHERE choice_id = '279' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2331' WHERE choice_id = '279' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2420' WHERE choice_id = '280' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2420' WHERE choice_id = '280' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2388' WHERE choice_id = '281' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2388' WHERE choice_id = '281' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1433' WHERE choice_id = '282' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1433' WHERE choice_id = '282' AND question_id = '20135' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 20139
UPDATE consultation_results SET choice_id = '1439' WHERE choice_id = '283' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1439' WHERE choice_id = '283' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1837' WHERE choice_id = '284' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1837' WHERE choice_id = '284' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1638' WHERE choice_id = '285' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1638' WHERE choice_id = '285' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2009' WHERE choice_id = '286' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2009' WHERE choice_id = '286' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2150' WHERE choice_id = '287' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2150' WHERE choice_id = '287' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2333' WHERE choice_id = '288' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2333' WHERE choice_id = '288' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2255' WHERE choice_id = '289' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2255' WHERE choice_id = '289' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2390' WHERE choice_id = '290' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2390' WHERE choice_id = '290' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2421' WHERE choice_id = '295' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2421' WHERE choice_id = '295' AND question_id = '20139' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 20140
UPDATE consultation_results SET choice_id = '1838' WHERE choice_id = '291' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1838' WHERE choice_id = '291' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2334' WHERE choice_id = '292' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2334' WHERE choice_id = '292' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2151' WHERE choice_id = '293' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2151' WHERE choice_id = '293' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2256' WHERE choice_id = '294' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2256' WHERE choice_id = '294' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2010' WHERE choice_id = '296' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2010' WHERE choice_id = '296' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2391' WHERE choice_id = '297' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2391' WHERE choice_id = '297' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2422' WHERE choice_id = '298' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2422' WHERE choice_id = '298' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2440' WHERE choice_id = '299' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2440' WHERE choice_id = '299' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1440' WHERE choice_id = '300' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1440' WHERE choice_id = '300' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1639' WHERE choice_id = '301' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1639' WHERE choice_id = '301' AND question_id = '20140' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 20141
UPDATE consultation_results SET choice_id = '1444' WHERE choice_id = '311' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1444' WHERE choice_id = '311' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1643' WHERE choice_id = '312' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1643' WHERE choice_id = '312' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1842' WHERE choice_id = '313' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1842' WHERE choice_id = '313' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2011' WHERE choice_id = '314' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2011' WHERE choice_id = '314' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2152' WHERE choice_id = '315' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2152' WHERE choice_id = '315' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2335' WHERE choice_id = '316' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2335' WHERE choice_id = '316' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2257' WHERE choice_id = '317' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2257' WHERE choice_id = '317' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2392' WHERE choice_id = '318' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2392' WHERE choice_id = '318' AND question_id = '20141' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 20142
UPDATE consultation_results SET choice_id = '2336' WHERE choice_id = '319' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2336' WHERE choice_id = '319' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1445' WHERE choice_id = '320' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1445' WHERE choice_id = '320' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1644' WHERE choice_id = '321' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1644' WHERE choice_id = '321' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2258' WHERE choice_id = '322' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2258' WHERE choice_id = '322' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1843' WHERE choice_id = '323' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1843' WHERE choice_id = '323' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2012' WHERE choice_id = '324' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2012' WHERE choice_id = '324' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2153' WHERE choice_id = '325' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2153' WHERE choice_id = '325' AND question_id = '20142' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation kmbc0ie5zsx94dejoiv1wcnv / question 20165
UPDATE consultation_results SET choice_id = '1686' WHERE choice_id = '348' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1686' WHERE choice_id = '348' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '1883' WHERE choice_id = '349' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1883' WHERE choice_id = '349' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2180' WHERE choice_id = '351' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2180' WHERE choice_id = '351' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2047' WHERE choice_id = '352' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2047' WHERE choice_id = '352' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '1487' WHERE choice_id = '353' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1487' WHERE choice_id = '353' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2282' WHERE choice_id = '354' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2282' WHERE choice_id = '354' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2354' WHERE choice_id = '355' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2354' WHERE choice_id = '355' AND question_id = '20165' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation kmbc0ie5zsx94dejoiv1wcnv / question 20166
UPDATE consultation_results SET choice_id = '1687' WHERE choice_id = '356' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1687' WHERE choice_id = '356' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '1488' WHERE choice_id = '357' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1488' WHERE choice_id = '357' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '1884' WHERE choice_id = '358' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1884' WHERE choice_id = '358' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2048' WHERE choice_id = '359' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2048' WHERE choice_id = '359' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2283' WHERE choice_id = '360' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2283' WHERE choice_id = '360' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2181' WHERE choice_id = '361' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2181' WHERE choice_id = '361' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2355' WHERE choice_id = '363' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2355' WHERE choice_id = '363' AND question_id = '20166' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation kmbc0ie5zsx94dejoiv1wcnv / question 20167
UPDATE consultation_results SET choice_id = '1489' WHERE choice_id = '362' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1489' WHERE choice_id = '362' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '1688' WHERE choice_id = '364' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1688' WHERE choice_id = '364' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '1885' WHERE choice_id = '365' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1885' WHERE choice_id = '365' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2049' WHERE choice_id = '366' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2049' WHERE choice_id = '366' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2182' WHERE choice_id = '367' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2182' WHERE choice_id = '367' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2284' WHERE choice_id = '368' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2284' WHERE choice_id = '368' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2356' WHERE choice_id = '369' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2356' WHERE choice_id = '369' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2405' WHERE choice_id = '370' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2405' WHERE choice_id = '370' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2444' WHERE choice_id = '371' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2444' WHERE choice_id = '371' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2429' WHERE choice_id = '372' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2429' WHERE choice_id = '372' AND question_id = '20167' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation kmbc0ie5zsx94dejoiv1wcnv / question 20169
UPDATE consultation_results SET choice_id = '1491' WHERE choice_id = '380' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1491' WHERE choice_id = '380' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '1690' WHERE choice_id = '381' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1690' WHERE choice_id = '381' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2051' WHERE choice_id = '382' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2051' WHERE choice_id = '382' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '1887' WHERE choice_id = '383' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1887' WHERE choice_id = '383' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2358' WHERE choice_id = '384' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2358' WHERE choice_id = '384' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2286' WHERE choice_id = '385' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2286' WHERE choice_id = '385' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2406' WHERE choice_id = '386' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2406' WHERE choice_id = '386' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2430' WHERE choice_id = '388' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2430' WHERE choice_id = '388' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2184' WHERE choice_id = '390' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2184' WHERE choice_id = '390' AND question_id = '20169' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation kmbc0ie5zsx94dejoiv1wcnv / question 20168
UPDATE consultation_results SET choice_id = '1490' WHERE choice_id = '373' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1490' WHERE choice_id = '373' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '1886' WHERE choice_id = '374' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1886' WHERE choice_id = '374' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2050' WHERE choice_id = '375' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2050' WHERE choice_id = '375' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '1689' WHERE choice_id = '376' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1689' WHERE choice_id = '376' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2183' WHERE choice_id = '377' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2183' WHERE choice_id = '377' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2285' WHERE choice_id = '378' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2285' WHERE choice_id = '378' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2357' WHERE choice_id = '379' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2357' WHERE choice_id = '379' AND question_id = '20168' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation w5d0rrhdlqby9shpt4wdk5oj / question 20154
UPDATE consultation_results SET choice_id = '1864' WHERE choice_id = '395' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '1864' WHERE choice_id = '395' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2168' WHERE choice_id = '396' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2168' WHERE choice_id = '396' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2030' WHERE choice_id = '397' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2030' WHERE choice_id = '397' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2272' WHERE choice_id = '398' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2272' WHERE choice_id = '398' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2344' WHERE choice_id = '399' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2344' WHERE choice_id = '399' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2397' WHERE choice_id = '400' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2397' WHERE choice_id = '400' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '1467' WHERE choice_id = '401' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '1467' WHERE choice_id = '401' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '1666' WHERE choice_id = '402' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '1666' WHERE choice_id = '402' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2425' WHERE choice_id = '403' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2425' WHERE choice_id = '403' AND question_id = '20154' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';

-- Consultation w5d0rrhdlqby9shpt4wdk5oj / question 20155
UPDATE consultation_results SET choice_id = '1470' WHERE choice_id = '410' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '1470' WHERE choice_id = '410' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '1669' WHERE choice_id = '413' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '1669' WHERE choice_id = '413' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '1867' WHERE choice_id = '414' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '1867' WHERE choice_id = '414' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2033' WHERE choice_id = '416' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2033' WHERE choice_id = '416' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2170' WHERE choice_id = '417' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2170' WHERE choice_id = '417' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2273' WHERE choice_id = '418' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2273' WHERE choice_id = '418' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2345' WHERE choice_id = '419' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2345' WHERE choice_id = '419' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2398' WHERE choice_id = '420' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2398' WHERE choice_id = '420' AND question_id = '20155' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';

-- Consultation w5d0rrhdlqby9shpt4wdk5oj / question 20157
UPDATE consultation_results SET choice_id = '2172' WHERE choice_id = '433' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2172' WHERE choice_id = '433' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2275' WHERE choice_id = '434' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2275' WHERE choice_id = '434' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2347' WHERE choice_id = '435' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2347' WHERE choice_id = '435' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2400' WHERE choice_id = '436' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2400' WHERE choice_id = '436' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2427' WHERE choice_id = '437' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2427' WHERE choice_id = '437' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '1472' WHERE choice_id = '438' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '1472' WHERE choice_id = '438' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2443' WHERE choice_id = '439' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2443' WHERE choice_id = '439' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '1671' WHERE choice_id = '440' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '1671' WHERE choice_id = '440' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2035' WHERE choice_id = '441' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2035' WHERE choice_id = '441' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '1869' WHERE choice_id = '442' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '1869' WHERE choice_id = '442' AND question_id = '20157' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';

-- Consultation w5d0rrhdlqby9shpt4wdk5oj / question 20156
UPDATE consultation_results SET choice_id = '1471' WHERE choice_id = '421' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '1471' WHERE choice_id = '421' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '1670' WHERE choice_id = '422' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '1670' WHERE choice_id = '422' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '1868' WHERE choice_id = '423' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '1868' WHERE choice_id = '423' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2034' WHERE choice_id = '424' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2034' WHERE choice_id = '424' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2171' WHERE choice_id = '425' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2171' WHERE choice_id = '425' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2346' WHERE choice_id = '426' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2346' WHERE choice_id = '426' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2274' WHERE choice_id = '427' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2274' WHERE choice_id = '427' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2399' WHERE choice_id = '428' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2399' WHERE choice_id = '428' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2426' WHERE choice_id = '429' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2426' WHERE choice_id = '429' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2442' WHERE choice_id = '430' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2442' WHERE choice_id = '430' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2452' WHERE choice_id = '431' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2452' WHERE choice_id = '431' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2457' WHERE choice_id = '432' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2457' WHERE choice_id = '432' AND question_id = '20156' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 20196
UPDATE consultation_results SET choice_id = '1757' WHERE choice_id = '484' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1757' WHERE choice_id = '484' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1558' WHERE choice_id = '485' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1558' WHERE choice_id = '485' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1947' WHERE choice_id = '486' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1947' WHERE choice_id = '486' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2099' WHERE choice_id = '487' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2099' WHERE choice_id = '487' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2215' WHERE choice_id = '488' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2215' WHERE choice_id = '488' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2310' WHERE choice_id = '489' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2310' WHERE choice_id = '489' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2373' WHERE choice_id = '490' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2373' WHERE choice_id = '490' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2415' WHERE choice_id = '491' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2415' WHERE choice_id = '491' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2436' WHERE choice_id = '492' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2436' WHERE choice_id = '492' AND question_id = '20196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 20197
UPDATE consultation_results SET choice_id = '1565' WHERE choice_id = '509' AND question_id = '20197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1565' WHERE choice_id = '509' AND question_id = '20197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1764' WHERE choice_id = '512' AND question_id = '20197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1764' WHERE choice_id = '512' AND question_id = '20197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1951' WHERE choice_id = '515' AND question_id = '20197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1951' WHERE choice_id = '515' AND question_id = '20197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2102' WHERE choice_id = '516' AND question_id = '20197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2102' WHERE choice_id = '516' AND question_id = '20197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 20209
UPDATE consultation_results SET choice_id = '1587' WHERE choice_id = '539' AND question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1587' WHERE choice_id = '539' AND question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1969' WHERE choice_id = '541' AND question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1969' WHERE choice_id = '541' AND question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2119' WHERE choice_id = '542' AND question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2119' WHERE choice_id = '542' AND question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1786' WHERE choice_id = '543' AND question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1786' WHERE choice_id = '543' AND question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2231' WHERE choice_id = '544' AND question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2231' WHERE choice_id = '544' AND question_id = '20209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 20210
UPDATE consultation_results SET choice_id = '1588' WHERE choice_id = '545' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1588' WHERE choice_id = '545' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2120' WHERE choice_id = '546' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2120' WHERE choice_id = '546' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1787' WHERE choice_id = '547' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1787' WHERE choice_id = '547' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1970' WHERE choice_id = '548' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1970' WHERE choice_id = '548' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2232' WHERE choice_id = '549' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2232' WHERE choice_id = '549' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2320' WHERE choice_id = '550' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2320' WHERE choice_id = '550' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2382' WHERE choice_id = '551' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2382' WHERE choice_id = '551' AND question_id = '20210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 20211
UPDATE consultation_results SET choice_id = '1596' WHERE choice_id = '571' AND question_id = '20211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1596' WHERE choice_id = '571' AND question_id = '20211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1795' WHERE choice_id = '572' AND question_id = '20211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1795' WHERE choice_id = '572' AND question_id = '20211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1974' WHERE choice_id = '573' AND question_id = '20211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1974' WHERE choice_id = '573' AND question_id = '20211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2122' WHERE choice_id = '574' AND question_id = '20211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2122' WHERE choice_id = '574' AND question_id = '20211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 20180
UPDATE consultation_results SET choice_id = '1514' WHERE choice_id = '588' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1514' WHERE choice_id = '588' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1907' WHERE choice_id = '589' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1907' WHERE choice_id = '589' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1713' WHERE choice_id = '590' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1713' WHERE choice_id = '590' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2067' WHERE choice_id = '591' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2067' WHERE choice_id = '591' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2196' WHERE choice_id = '592' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2196' WHERE choice_id = '592' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2295' WHERE choice_id = '593' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2295' WHERE choice_id = '593' AND question_id = '20180' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 20181
UPDATE consultation_results SET choice_id = '1517' WHERE choice_id = '603' AND question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1517' WHERE choice_id = '603' AND question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1716' WHERE choice_id = '604' AND question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1716' WHERE choice_id = '604' AND question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2069' WHERE choice_id = '605' AND question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2069' WHERE choice_id = '605' AND question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1910' WHERE choice_id = '606' AND question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1910' WHERE choice_id = '606' AND question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2198' WHERE choice_id = '608' AND question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2198' WHERE choice_id = '608' AND question_id = '20181' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 20182
UPDATE consultation_results SET choice_id = '1518' WHERE choice_id = '607' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1518' WHERE choice_id = '607' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1717' WHERE choice_id = '609' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1717' WHERE choice_id = '609' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1911' WHERE choice_id = '610' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1911' WHERE choice_id = '610' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2070' WHERE choice_id = '611' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2070' WHERE choice_id = '611' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2297' WHERE choice_id = '612' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2297' WHERE choice_id = '612' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2199' WHERE choice_id = '614' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2199' WHERE choice_id = '614' AND question_id = '20182' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 20183
UPDATE consultation_results SET choice_id = '1521' WHERE choice_id = '623' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1521' WHERE choice_id = '623' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1914' WHERE choice_id = '624' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1914' WHERE choice_id = '624' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1720' WHERE choice_id = '625' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1720' WHERE choice_id = '625' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2073' WHERE choice_id = '626' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2073' WHERE choice_id = '626' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2201' WHERE choice_id = '627' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2201' WHERE choice_id = '627' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2363' WHERE choice_id = '628' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2363' WHERE choice_id = '628' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2298' WHERE choice_id = '630' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2298' WHERE choice_id = '630' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2409' WHERE choice_id = '631' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2409' WHERE choice_id = '631' AND question_id = '20183' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation vkp77x0ognt41xyjicz9wwba / question 20199
UPDATE consultation_results SET choice_id = '2108' WHERE choice_id = '645' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '2108' WHERE choice_id = '645' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '1958' WHERE choice_id = '646' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '1958' WHERE choice_id = '646' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '1575' WHERE choice_id = '647' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '1575' WHERE choice_id = '647' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '1774' WHERE choice_id = '648' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '1774' WHERE choice_id = '648' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '2311' WHERE choice_id = '649' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '2311' WHERE choice_id = '649' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '2221' WHERE choice_id = '650' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '2221' WHERE choice_id = '650' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '2374' WHERE choice_id = '652' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '2374' WHERE choice_id = '652' AND question_id = '20199' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';

-- Consultation vkp77x0ognt41xyjicz9wwba / question 20198
UPDATE consultation_results SET choice_id = '1574' WHERE choice_id = '640' AND question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '1574' WHERE choice_id = '640' AND question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '1773' WHERE choice_id = '641' AND question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '1773' WHERE choice_id = '641' AND question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '1957' WHERE choice_id = '642' AND question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '1957' WHERE choice_id = '642' AND question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '2107' WHERE choice_id = '643' AND question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '2107' WHERE choice_id = '643' AND question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '2220' WHERE choice_id = '644' AND question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '2220' WHERE choice_id = '644' AND question_id = '20198' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';

-- Consultation vkp77x0ognt41xyjicz9wwba / question 20200
UPDATE consultation_results SET choice_id = '1576' WHERE choice_id = '651' AND question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '1576' WHERE choice_id = '651' AND question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '1775' WHERE choice_id = '653' AND question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '1775' WHERE choice_id = '653' AND question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '1959' WHERE choice_id = '654' AND question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '1959' WHERE choice_id = '654' AND question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '2109' WHERE choice_id = '655' AND question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '2109' WHERE choice_id = '655' AND question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '2222' WHERE choice_id = '661' AND question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '2222' WHERE choice_id = '661' AND question_id = '20200' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';

-- Consultation vkp77x0ognt41xyjicz9wwba / question 20201
UPDATE consultation_results SET choice_id = '1577' WHERE choice_id = '656' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '1577' WHERE choice_id = '656' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '1776' WHERE choice_id = '657' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '1776' WHERE choice_id = '657' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '2110' WHERE choice_id = '658' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '2110' WHERE choice_id = '658' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '2223' WHERE choice_id = '659' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '2223' WHERE choice_id = '659' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '1960' WHERE choice_id = '660' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '1960' WHERE choice_id = '660' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '2312' WHERE choice_id = '662' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '2312' WHERE choice_id = '662' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '2375' WHERE choice_id = '663' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '2375' WHERE choice_id = '663' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '2416' WHERE choice_id = '665' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '2416' WHERE choice_id = '665' AND question_id = '20201' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';

-- Consultation rn3ib6tpe4yv3l7ofolyqh7d / question 20213
UPDATE consultation_results SET choice_id = '1601' WHERE choice_id = '669' AND question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1601' WHERE choice_id = '669' AND question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '1979' WHERE choice_id = '670' AND question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1979' WHERE choice_id = '670' AND question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '1800' WHERE choice_id = '671' AND question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1800' WHERE choice_id = '671' AND question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '2125' WHERE choice_id = '672' AND question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '2125' WHERE choice_id = '672' AND question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '2236' WHERE choice_id = '673' AND question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '2236' WHERE choice_id = '673' AND question_id = '20213' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';

-- Consultation rn3ib6tpe4yv3l7ofolyqh7d / question 20214
UPDATE consultation_results SET choice_id = '1804' WHERE choice_id = '684' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1804' WHERE choice_id = '684' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '1983' WHERE choice_id = '685' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1983' WHERE choice_id = '685' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '1605' WHERE choice_id = '686' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1605' WHERE choice_id = '686' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '2127' WHERE choice_id = '687' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '2127' WHERE choice_id = '687' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '2237' WHERE choice_id = '688' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '2237' WHERE choice_id = '688' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '2322' WHERE choice_id = '689' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '2322' WHERE choice_id = '689' AND question_id = '20214' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';

-- Consultation rn3ib6tpe4yv3l7ofolyqh7d / question 20215
UPDATE consultation_results SET choice_id = '1606' WHERE choice_id = '690' AND question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1606' WHERE choice_id = '690' AND question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '1805' WHERE choice_id = '691' AND question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1805' WHERE choice_id = '691' AND question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '2128' WHERE choice_id = '692' AND question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '2128' WHERE choice_id = '692' AND question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '1984' WHERE choice_id = '693' AND question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1984' WHERE choice_id = '693' AND question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '2238' WHERE choice_id = '694' AND question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '2238' WHERE choice_id = '694' AND question_id = '20215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';

-- Consultation rn3ib6tpe4yv3l7ofolyqh7d / question 20216
UPDATE consultation_results SET choice_id = '1806' WHERE choice_id = '695' AND question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1806' WHERE choice_id = '695' AND question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '1985' WHERE choice_id = '696' AND question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1985' WHERE choice_id = '696' AND question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '1607' WHERE choice_id = '697' AND question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1607' WHERE choice_id = '697' AND question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '2129' WHERE choice_id = '698' AND question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '2129' WHERE choice_id = '698' AND question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '2239' WHERE choice_id = '699' AND question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '2239' WHERE choice_id = '699' AND question_id = '20216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 20217
UPDATE consultation_results SET choice_id = '1810' WHERE choice_id = '703' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1810' WHERE choice_id = '703' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1611' WHERE choice_id = '704' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1611' WHERE choice_id = '704' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1986' WHERE choice_id = '706' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1986' WHERE choice_id = '706' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '2130' WHERE choice_id = '707' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '2130' WHERE choice_id = '707' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '2240' WHERE choice_id = '708' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '2240' WHERE choice_id = '708' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '2383' WHERE choice_id = '709' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '2383' WHERE choice_id = '709' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '2323' WHERE choice_id = '710' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '2323' WHERE choice_id = '710' AND question_id = '20217' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 20219
UPDATE consultation_results SET choice_id = '1613' WHERE choice_id = '717' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1613' WHERE choice_id = '717' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1812' WHERE choice_id = '718' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1812' WHERE choice_id = '718' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1988' WHERE choice_id = '719' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1988' WHERE choice_id = '719' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '2132' WHERE choice_id = '720' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '2132' WHERE choice_id = '720' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '2242' WHERE choice_id = '721' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '2242' WHERE choice_id = '721' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '2324' WHERE choice_id = '722' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '2324' WHERE choice_id = '722' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '2384' WHERE choice_id = '725' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '2384' WHERE choice_id = '725' AND question_id = '20219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 20218
UPDATE consultation_results SET choice_id = '1811' WHERE choice_id = '712' AND question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1811' WHERE choice_id = '712' AND question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1612' WHERE choice_id = '713' AND question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1612' WHERE choice_id = '713' AND question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1987' WHERE choice_id = '714' AND question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1987' WHERE choice_id = '714' AND question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '2131' WHERE choice_id = '715' AND question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '2131' WHERE choice_id = '715' AND question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '2241' WHERE choice_id = '716' AND question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '2241' WHERE choice_id = '716' AND question_id = '20218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 20221
UPDATE consultation_results SET choice_id = '1818' WHERE choice_id = '744' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1818' WHERE choice_id = '744' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1619' WHERE choice_id = '745' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1619' WHERE choice_id = '745' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1993' WHERE choice_id = '746' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1993' WHERE choice_id = '746' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '2244' WHERE choice_id = '747' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '2244' WHERE choice_id = '747' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '2134' WHERE choice_id = '748' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '2134' WHERE choice_id = '748' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '2326' WHERE choice_id = '749' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '2326' WHERE choice_id = '749' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '2386' WHERE choice_id = '751' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '2386' WHERE choice_id = '751' AND question_id = '20221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 20220
UPDATE consultation_results SET choice_id = '1618' WHERE choice_id = '737' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1618' WHERE choice_id = '737' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1817' WHERE choice_id = '738' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1817' WHERE choice_id = '738' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1992' WHERE choice_id = '739' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1992' WHERE choice_id = '739' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '2133' WHERE choice_id = '740' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '2133' WHERE choice_id = '740' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '2325' WHERE choice_id = '741' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '2325' WHERE choice_id = '741' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '2243' WHERE choice_id = '742' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '2243' WHERE choice_id = '742' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '2385' WHERE choice_id = '743' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '2385' WHERE choice_id = '743' AND question_id = '20220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 20222
UPDATE consultation_results SET choice_id = '1620' WHERE choice_id = '756' AND question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1620' WHERE choice_id = '756' AND question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '2245' WHERE choice_id = '757' AND question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '2245' WHERE choice_id = '757' AND question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1819' WHERE choice_id = '759' AND question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1819' WHERE choice_id = '759' AND question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '2135' WHERE choice_id = '760' AND question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '2135' WHERE choice_id = '760' AND question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1994' WHERE choice_id = '762' AND question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1994' WHERE choice_id = '762' AND question_id = '20222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation jf84upj9qutugwkce773m8dt / question 20187
UPDATE consultation_results SET choice_id = '1727' WHERE choice_id = '1017' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1727' WHERE choice_id = '1017' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1528' WHERE choice_id = '1018' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1528' WHERE choice_id = '1018' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2079' WHERE choice_id = '1019' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2079' WHERE choice_id = '1019' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1921' WHERE choice_id = '1020' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1921' WHERE choice_id = '1020' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2302' WHERE choice_id = '1021' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2302' WHERE choice_id = '1021' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2205' WHERE choice_id = '1022' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2205' WHERE choice_id = '1022' AND question_id = '20187' AND consultation_id = 'jf84upj9qutugwkce773m8dt';

-- Consultation jf84upj9qutugwkce773m8dt / question 20185
UPDATE consultation_results SET choice_id = '1917' WHERE choice_id = '1023' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1917' WHERE choice_id = '1023' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1524' WHERE choice_id = '1024' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1524' WHERE choice_id = '1024' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1723' WHERE choice_id = '1025' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1723' WHERE choice_id = '1025' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2203' WHERE choice_id = '1026' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2203' WHERE choice_id = '1026' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2076' WHERE choice_id = '1027' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2076' WHERE choice_id = '1027' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2365' WHERE choice_id = '1028' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2365' WHERE choice_id = '1028' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2300' WHERE choice_id = '1029' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2300' WHERE choice_id = '1029' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2410' WHERE choice_id = '1030' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2410' WHERE choice_id = '1030' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2433' WHERE choice_id = '1031' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2433' WHERE choice_id = '1031' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2446' WHERE choice_id = '1036' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2446' WHERE choice_id = '1036' AND question_id = '20185' AND consultation_id = 'jf84upj9qutugwkce773m8dt';

-- Consultation jf84upj9qutugwkce773m8dt / question 20192
UPDATE consultation_results SET choice_id = '1543' WHERE choice_id = '1032' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1543' WHERE choice_id = '1032' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1742' WHERE choice_id = '1033' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1742' WHERE choice_id = '1033' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1936' WHERE choice_id = '1034' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1936' WHERE choice_id = '1034' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2091' WHERE choice_id = '1035' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2091' WHERE choice_id = '1035' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2306' WHERE choice_id = '1037' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2306' WHERE choice_id = '1037' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2369' WHERE choice_id = '1038' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2369' WHERE choice_id = '1038' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2210' WHERE choice_id = '1039' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2210' WHERE choice_id = '1039' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2413' WHERE choice_id = '1043' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2413' WHERE choice_id = '1043' AND question_id = '20192' AND consultation_id = 'jf84upj9qutugwkce773m8dt';

-- Consultation jf84upj9qutugwkce773m8dt / question 20193
UPDATE consultation_results SET choice_id = '1937' WHERE choice_id = '1070' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1937' WHERE choice_id = '1070' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1544' WHERE choice_id = '1071' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1544' WHERE choice_id = '1071' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1743' WHERE choice_id = '1072' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1743' WHERE choice_id = '1072' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2211' WHERE choice_id = '1073' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2211' WHERE choice_id = '1073' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2092' WHERE choice_id = '1074' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2092' WHERE choice_id = '1074' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2307' WHERE choice_id = '1075' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2307' WHERE choice_id = '1075' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2370' WHERE choice_id = '1076' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2370' WHERE choice_id = '1076' AND question_id = '20193' AND consultation_id = 'jf84upj9qutugwkce773m8dt';

-- Consultation jf84upj9qutugwkce773m8dt / question 20188
UPDATE consultation_results SET choice_id = '1529' WHERE choice_id = '1044' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1529' WHERE choice_id = '1044' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1922' WHERE choice_id = '1045' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1922' WHERE choice_id = '1045' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1728' WHERE choice_id = '1046' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1728' WHERE choice_id = '1046' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2080' WHERE choice_id = '1047' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2080' WHERE choice_id = '1047' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2206' WHERE choice_id = '1048' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2206' WHERE choice_id = '1048' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2303' WHERE choice_id = '1049' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2303' WHERE choice_id = '1049' AND question_id = '20188' AND consultation_id = 'jf84upj9qutugwkce773m8dt';

-- Consultation jf84upj9qutugwkce773m8dt / question 20184
UPDATE consultation_results SET choice_id = '1523' WHERE choice_id = '1050' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1523' WHERE choice_id = '1050' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1722' WHERE choice_id = '1051' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1722' WHERE choice_id = '1051' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1916' WHERE choice_id = '1052' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1916' WHERE choice_id = '1052' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2299' WHERE choice_id = '1053' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2299' WHERE choice_id = '1053' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2364' WHERE choice_id = '1054' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2364' WHERE choice_id = '1054' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2075' WHERE choice_id = '1055' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2075' WHERE choice_id = '1055' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2202' WHERE choice_id = '1056' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2202' WHERE choice_id = '1056' AND question_id = '20184' AND consultation_id = 'jf84upj9qutugwkce773m8dt';

-- Consultation jf84upj9qutugwkce773m8dt / question 20194
UPDATE consultation_results SET choice_id = '1545' WHERE choice_id = '1057' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1545' WHERE choice_id = '1057' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1744' WHERE choice_id = '1058' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1744' WHERE choice_id = '1058' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1938' WHERE choice_id = '1059' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1938' WHERE choice_id = '1059' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2212' WHERE choice_id = '1060' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2212' WHERE choice_id = '1060' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2093' WHERE choice_id = '1061' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2093' WHERE choice_id = '1061' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2308' WHERE choice_id = '1062' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2308' WHERE choice_id = '1062' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2371' WHERE choice_id = '1063' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2371' WHERE choice_id = '1063' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2414' WHERE choice_id = '1064' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2414' WHERE choice_id = '1064' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2435' WHERE choice_id = '1065' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2435' WHERE choice_id = '1065' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2448' WHERE choice_id = '1066' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2448' WHERE choice_id = '1066' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2454' WHERE choice_id = '1067' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2454' WHERE choice_id = '1067' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2459' WHERE choice_id = '1068' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2459' WHERE choice_id = '1068' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2463' WHERE choice_id = '1069' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2463' WHERE choice_id = '1069' AND question_id = '20194' AND consultation_id = 'jf84upj9qutugwkce773m8dt';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 20137
UPDATE consultation_results SET choice_id = '1436' WHERE choice_id = '1080' AND question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1436' WHERE choice_id = '1080' AND question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '2006' WHERE choice_id = '1081' AND question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '2006' WHERE choice_id = '1081' AND question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1635' WHERE choice_id = '1082' AND question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1635' WHERE choice_id = '1082' AND question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1834' WHERE choice_id = '1083' AND question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1834' WHERE choice_id = '1083' AND question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '2147' WHERE choice_id = '1084' AND question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '2147' WHERE choice_id = '1084' AND question_id = '20137' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 20138
UPDATE consultation_results SET choice_id = '1437' WHERE choice_id = '1090' AND question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1437' WHERE choice_id = '1090' AND question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1636' WHERE choice_id = '1091' AND question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1636' WHERE choice_id = '1091' AND question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1835' WHERE choice_id = '1093' AND question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1835' WHERE choice_id = '1093' AND question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '2007' WHERE choice_id = '1095' AND question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '2007' WHERE choice_id = '1095' AND question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '2148' WHERE choice_id = '1096' AND question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '2148' WHERE choice_id = '1096' AND question_id = '20138' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 20178
UPDATE consultation_results SET choice_id = '1706' WHERE choice_id = '1109' AND question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1706' WHERE choice_id = '1109' AND question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1507' WHERE choice_id = '1110' AND question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1507' WHERE choice_id = '1110' AND question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '2062' WHERE choice_id = '1111' AND question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '2062' WHERE choice_id = '1111' AND question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1901' WHERE choice_id = '1112' AND question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1901' WHERE choice_id = '1112' AND question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '2194' WHERE choice_id = '1113' AND question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '2194' WHERE choice_id = '1113' AND question_id = '20178' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 20143
UPDATE consultation_results SET choice_id = '1447' WHERE choice_id = '1102' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1447' WHERE choice_id = '1102' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1845' WHERE choice_id = '1103' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1845' WHERE choice_id = '1103' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1646' WHERE choice_id = '1104' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1646' WHERE choice_id = '1104' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '2014' WHERE choice_id = '1105' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '2014' WHERE choice_id = '1105' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '2154' WHERE choice_id = '1106' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '2154' WHERE choice_id = '1106' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '2259' WHERE choice_id = '1107' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '2259' WHERE choice_id = '1107' AND question_id = '20143' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 20179
UPDATE consultation_results SET choice_id = '1509' WHERE choice_id = '1118' AND question_id = '20179' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1509' WHERE choice_id = '1118' AND question_id = '20179' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1903' WHERE choice_id = '1121' AND question_id = '20179' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1903' WHERE choice_id = '1121' AND question_id = '20179' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1708' WHERE choice_id = '1122' AND question_id = '20179' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1708' WHERE choice_id = '1122' AND question_id = '20179' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '2064' WHERE choice_id = '1123' AND question_id = '20179' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '2064' WHERE choice_id = '1123' AND question_id = '20179' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 20203
UPDATE consultation_results SET choice_id = '1580' WHERE choice_id = '1163' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1580' WHERE choice_id = '1163' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1779' WHERE choice_id = '1164' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1779' WHERE choice_id = '1164' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1963' WHERE choice_id = '1165' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1963' WHERE choice_id = '1165' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '2113' WHERE choice_id = '1166' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '2113' WHERE choice_id = '1166' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '2225' WHERE choice_id = '1167' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '2225' WHERE choice_id = '1167' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '2377' WHERE choice_id = '1168' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '2377' WHERE choice_id = '1168' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '2314' WHERE choice_id = '1169' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '2314' WHERE choice_id = '1169' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '2417' WHERE choice_id = '1170' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '2417' WHERE choice_id = '1170' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '2437' WHERE choice_id = '1181' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '2437' WHERE choice_id = '1181' AND question_id = '20203' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 20204
UPDATE consultation_results SET choice_id = '2315' WHERE choice_id = '1171' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '2315' WHERE choice_id = '1171' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '2438' WHERE choice_id = '1172' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '2438' WHERE choice_id = '1172' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '2418' WHERE choice_id = '1173' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '2418' WHERE choice_id = '1173' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '2378' WHERE choice_id = '1174' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '2378' WHERE choice_id = '1174' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1581' WHERE choice_id = '1175' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1581' WHERE choice_id = '1175' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1780' WHERE choice_id = '1176' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1780' WHERE choice_id = '1176' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1964' WHERE choice_id = '1177' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1964' WHERE choice_id = '1177' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '2114' WHERE choice_id = '1178' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '2114' WHERE choice_id = '1178' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '2449' WHERE choice_id = '1179' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '2449' WHERE choice_id = '1179' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '2226' WHERE choice_id = '1180' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '2226' WHERE choice_id = '1180' AND question_id = '20204' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 20205
UPDATE consultation_results SET choice_id = '1582' WHERE choice_id = '1182' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1582' WHERE choice_id = '1182' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1781' WHERE choice_id = '1183' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1781' WHERE choice_id = '1183' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '1965' WHERE choice_id = '1184' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '1965' WHERE choice_id = '1184' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '2115' WHERE choice_id = '1185' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '2115' WHERE choice_id = '1185' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '2227' WHERE choice_id = '1186' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '2227' WHERE choice_id = '1186' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '2316' WHERE choice_id = '1187' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '2316' WHERE choice_id = '1187' AND question_id = '20205' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation esg67bdrkzh0n29j2c858ryj / question 20186
UPDATE consultation_results SET choice_id = '2077' WHERE choice_id = '1213' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2077' WHERE choice_id = '1213' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1918' WHERE choice_id = '1214' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1918' WHERE choice_id = '1214' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2204' WHERE choice_id = '1215' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2204' WHERE choice_id = '1215' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2301' WHERE choice_id = '1216' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2301' WHERE choice_id = '1216' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2366' WHERE choice_id = '1217' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2366' WHERE choice_id = '1217' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2411' WHERE choice_id = '1218' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2411' WHERE choice_id = '1218' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1525' WHERE choice_id = '1219' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1525' WHERE choice_id = '1219' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1724' WHERE choice_id = '1220' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1724' WHERE choice_id = '1220' AND question_id = '20186' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';

-- Consultation esg67bdrkzh0n29j2c858ryj / question 20202
UPDATE consultation_results SET choice_id = '1579' WHERE choice_id = '1228' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1579' WHERE choice_id = '1228' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1778' WHERE choice_id = '1229' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1778' WHERE choice_id = '1229' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1962' WHERE choice_id = '1230' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1962' WHERE choice_id = '1230' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2112' WHERE choice_id = '1231' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2112' WHERE choice_id = '1231' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2313' WHERE choice_id = '1232' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2313' WHERE choice_id = '1232' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2376' WHERE choice_id = '1233' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2376' WHERE choice_id = '1233' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2224' WHERE choice_id = '1234' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2224' WHERE choice_id = '1234' AND question_id = '20202' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';

-- Consultation esg67bdrkzh0n29j2c858ryj / question 20195
UPDATE consultation_results SET choice_id = '1554' WHERE choice_id = '1221' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1554' WHERE choice_id = '1221' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1753' WHERE choice_id = '1222' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1753' WHERE choice_id = '1222' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2097' WHERE choice_id = '1223' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2097' WHERE choice_id = '1223' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1945' WHERE choice_id = '1224' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1945' WHERE choice_id = '1224' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2309' WHERE choice_id = '1225' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2309' WHERE choice_id = '1225' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2214' WHERE choice_id = '1226' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2214' WHERE choice_id = '1226' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2372' WHERE choice_id = '1227' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2372' WHERE choice_id = '1227' AND question_id = '20195' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';

-- Consultation esg67bdrkzh0n29j2c858ryj / question 20206
UPDATE consultation_results SET choice_id = '2228' WHERE choice_id = '1235' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2228' WHERE choice_id = '1235' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2317' WHERE choice_id = '1250' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2317' WHERE choice_id = '1250' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2379' WHERE choice_id = '1251' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2379' WHERE choice_id = '1251' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2419' WHERE choice_id = '1252' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2419' WHERE choice_id = '1252' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2439' WHERE choice_id = '1253' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2439' WHERE choice_id = '1253' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2450' WHERE choice_id = '1254' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2450' WHERE choice_id = '1254' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2455' WHERE choice_id = '1255' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2455' WHERE choice_id = '1255' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2460' WHERE choice_id = '1256' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2460' WHERE choice_id = '1256' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2464' WHERE choice_id = '1257' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2464' WHERE choice_id = '1257' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2466' WHERE choice_id = '1258' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2466' WHERE choice_id = '1258' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2468' WHERE choice_id = '1259' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2468' WHERE choice_id = '1259' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2470' WHERE choice_id = '1260' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2470' WHERE choice_id = '1260' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1583' WHERE choice_id = '1261' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1583' WHERE choice_id = '1261' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1782' WHERE choice_id = '1262' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1782' WHERE choice_id = '1262' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1966' WHERE choice_id = '1263' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1966' WHERE choice_id = '1263' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2116' WHERE choice_id = '1264' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2116' WHERE choice_id = '1264' AND question_id = '20206' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';

-- Consultation esg67bdrkzh0n29j2c858ryj / question 20208
UPDATE consultation_results SET choice_id = '1784' WHERE choice_id = '1241' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1784' WHERE choice_id = '1241' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1585' WHERE choice_id = '1242' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1585' WHERE choice_id = '1242' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1968' WHERE choice_id = '1245' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1968' WHERE choice_id = '1245' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2118' WHERE choice_id = '1246' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2118' WHERE choice_id = '1246' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2230' WHERE choice_id = '1247' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2230' WHERE choice_id = '1247' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2319' WHERE choice_id = '1248' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2319' WHERE choice_id = '1248' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2381' WHERE choice_id = '1249' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2381' WHERE choice_id = '1249' AND question_id = '20208' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';

-- Consultation esg67bdrkzh0n29j2c858ryj / question 20207
UPDATE consultation_results SET choice_id = '1967' WHERE choice_id = '1236' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1967' WHERE choice_id = '1236' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1584' WHERE choice_id = '1237' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1584' WHERE choice_id = '1237' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '1783' WHERE choice_id = '1238' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '1783' WHERE choice_id = '1238' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2117' WHERE choice_id = '1239' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2117' WHERE choice_id = '1239' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2318' WHERE choice_id = '1240' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2318' WHERE choice_id = '1240' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2229' WHERE choice_id = '1243' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2229' WHERE choice_id = '1243' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE consultation_results SET choice_id = '2380' WHERE choice_id = '1244' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';
UPDATE reponses_consultation SET choice_id = '2380' WHERE choice_id = '1244' AND question_id = '20207' AND consultation_id = 'esg67bdrkzh0n29j2c858ryj';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 20212
UPDATE consultation_results SET choice_id = '1598' WHERE choice_id = '1303' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1598' WHERE choice_id = '1303' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1797' WHERE choice_id = '1304' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1797' WHERE choice_id = '1304' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1976' WHERE choice_id = '1305' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1976' WHERE choice_id = '1305' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '2123' WHERE choice_id = '1306' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '2123' WHERE choice_id = '1306' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '2234' WHERE choice_id = '1307' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '2234' WHERE choice_id = '1307' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '2321' WHERE choice_id = '1308' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '2321' WHERE choice_id = '1308' AND question_id = '20212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 20223
UPDATE consultation_results SET choice_id = '1621' WHERE choice_id = '1316' AND question_id = '20223' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1621' WHERE choice_id = '1316' AND question_id = '20223' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '2136' WHERE choice_id = '1318' AND question_id = '20223' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '2136' WHERE choice_id = '1318' AND question_id = '20223' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1995' WHERE choice_id = '1320' AND question_id = '20223' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1995' WHERE choice_id = '1320' AND question_id = '20223' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1820' WHERE choice_id = '1321' AND question_id = '20223' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1820' WHERE choice_id = '1321' AND question_id = '20223' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 20224
UPDATE consultation_results SET choice_id = '1622' WHERE choice_id = '1319' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1622' WHERE choice_id = '1319' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1821' WHERE choice_id = '1322' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1821' WHERE choice_id = '1322' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '2137' WHERE choice_id = '1323' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '2137' WHERE choice_id = '1323' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '2327' WHERE choice_id = '1324' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '2327' WHERE choice_id = '1324' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '2246' WHERE choice_id = '1325' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '2246' WHERE choice_id = '1325' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1996' WHERE choice_id = '1326' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1996' WHERE choice_id = '1326' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '2387' WHERE choice_id = '1327' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '2387' WHERE choice_id = '1327' AND question_id = '20224' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 20226
UPDATE consultation_results SET choice_id = '1625' WHERE choice_id = '1338' AND question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1625' WHERE choice_id = '1338' AND question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1824' WHERE choice_id = '1340' AND question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1824' WHERE choice_id = '1340' AND question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1999' WHERE choice_id = '1341' AND question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1999' WHERE choice_id = '1341' AND question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '2140' WHERE choice_id = '1342' AND question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '2140' WHERE choice_id = '1342' AND question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '2249' WHERE choice_id = '1343' AND question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '2249' WHERE choice_id = '1343' AND question_id = '20226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 20225
UPDATE consultation_results SET choice_id = '1624' WHERE choice_id = '1334' AND question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1624' WHERE choice_id = '1334' AND question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1823' WHERE choice_id = '1335' AND question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1823' WHERE choice_id = '1335' AND question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1998' WHERE choice_id = '1336' AND question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1998' WHERE choice_id = '1336' AND question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '2139' WHERE choice_id = '1337' AND question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '2139' WHERE choice_id = '1337' AND question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '2248' WHERE choice_id = '1339' AND question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '2248' WHERE choice_id = '1339' AND question_id = '20225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 20136
UPDATE consultation_results SET choice_id = '1434' WHERE choice_id = '1356' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1434' WHERE choice_id = '1356' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2146' WHERE choice_id = '1364' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2146' WHERE choice_id = '1364' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2254' WHERE choice_id = '1365' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2254' WHERE choice_id = '1365' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2389' WHERE choice_id = '1366' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2389' WHERE choice_id = '1366' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2332' WHERE choice_id = '1367' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2332' WHERE choice_id = '1367' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1832' WHERE choice_id = '1368' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1832' WHERE choice_id = '1368' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2005' WHERE choice_id = '1369' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2005' WHERE choice_id = '1369' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1633' WHERE choice_id = '1370' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1633' WHERE choice_id = '1370' AND question_id = '20136' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 20159
UPDATE consultation_results SET choice_id = '1474' WHERE choice_id = '1357' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1474' WHERE choice_id = '1357' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1673' WHERE choice_id = '1358' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1673' WHERE choice_id = '1358' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1871' WHERE choice_id = '1359' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1871' WHERE choice_id = '1359' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2174' WHERE choice_id = '1360' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2174' WHERE choice_id = '1360' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2037' WHERE choice_id = '1361' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2037' WHERE choice_id = '1361' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2277' WHERE choice_id = '1362' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2277' WHERE choice_id = '1362' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2349' WHERE choice_id = '1363' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2349' WHERE choice_id = '1363' AND question_id = '20159' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 20164
UPDATE consultation_results SET choice_id = '1682' WHERE choice_id = '1371' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1682' WHERE choice_id = '1371' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1483' WHERE choice_id = '1372' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1483' WHERE choice_id = '1372' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1879' WHERE choice_id = '1386' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1879' WHERE choice_id = '1386' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2178' WHERE choice_id = '1387' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2178' WHERE choice_id = '1387' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2281' WHERE choice_id = '1388' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2281' WHERE choice_id = '1388' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2043' WHERE choice_id = '1389' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2043' WHERE choice_id = '1389' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2353' WHERE choice_id = '1390' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2353' WHERE choice_id = '1390' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2404' WHERE choice_id = '1391' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2404' WHERE choice_id = '1391' AND question_id = '20164' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 20170
UPDATE consultation_results SET choice_id = '1692' WHERE choice_id = '1373' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1692' WHERE choice_id = '1373' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1493' WHERE choice_id = '1374' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1493' WHERE choice_id = '1374' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1889' WHERE choice_id = '1375' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1889' WHERE choice_id = '1375' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2053' WHERE choice_id = '1376' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2053' WHERE choice_id = '1376' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2186' WHERE choice_id = '1377' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2186' WHERE choice_id = '1377' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2288' WHERE choice_id = '1378' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2288' WHERE choice_id = '1378' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2359' WHERE choice_id = '1380' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2359' WHERE choice_id = '1380' AND question_id = '20170' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 20171
UPDATE consultation_results SET choice_id = '1494' WHERE choice_id = '1379' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1494' WHERE choice_id = '1379' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1890' WHERE choice_id = '1381' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1890' WHERE choice_id = '1381' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1693' WHERE choice_id = '1382' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1693' WHERE choice_id = '1382' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2187' WHERE choice_id = '1383' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2187' WHERE choice_id = '1383' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2054' WHERE choice_id = '1384' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2054' WHERE choice_id = '1384' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2289' WHERE choice_id = '1385' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2289' WHERE choice_id = '1385' AND question_id = '20171' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 20174
UPDATE consultation_results SET choice_id = '1501' WHERE choice_id = '1392' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1501' WHERE choice_id = '1392' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1700' WHERE choice_id = '1393' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1700' WHERE choice_id = '1393' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1895' WHERE choice_id = '1394' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1895' WHERE choice_id = '1394' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2057' WHERE choice_id = '1395' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2057' WHERE choice_id = '1395' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2292' WHERE choice_id = '1396' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2292' WHERE choice_id = '1396' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2361' WHERE choice_id = '1397' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2361' WHERE choice_id = '1397' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2407' WHERE choice_id = '1398' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2407' WHERE choice_id = '1398' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2190' WHERE choice_id = '1399' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2190' WHERE choice_id = '1399' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2431' WHERE choice_id = '1400' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2431' WHERE choice_id = '1400' AND question_id = '20174' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 20175
UPDATE consultation_results SET choice_id = '1502' WHERE choice_id = '1401' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1502' WHERE choice_id = '1401' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1896' WHERE choice_id = '1402' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1896' WHERE choice_id = '1402' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1701' WHERE choice_id = '1403' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1701' WHERE choice_id = '1403' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2058' WHERE choice_id = '1404' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2058' WHERE choice_id = '1404' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2191' WHERE choice_id = '1405' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2191' WHERE choice_id = '1405' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2293' WHERE choice_id = '1406' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2293' WHERE choice_id = '1406' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2362' WHERE choice_id = '1407' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2362' WHERE choice_id = '1407' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2408' WHERE choice_id = '1408' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2408' WHERE choice_id = '1408' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2432' WHERE choice_id = '1409' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2432' WHERE choice_id = '1409' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2445' WHERE choice_id = '1411' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2445' WHERE choice_id = '1411' AND question_id = '20175' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 20176
UPDATE consultation_results SET choice_id = '1503' WHERE choice_id = '1410' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1503' WHERE choice_id = '1410' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1702' WHERE choice_id = '1412' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1702' WHERE choice_id = '1412' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1897' WHERE choice_id = '1413' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1897' WHERE choice_id = '1413' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2059' WHERE choice_id = '1414' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2059' WHERE choice_id = '1414' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2192' WHERE choice_id = '1415' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2192' WHERE choice_id = '1415' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2294' WHERE choice_id = '1419' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2294' WHERE choice_id = '1419' AND question_id = '20176' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 20177
UPDATE consultation_results SET choice_id = '1506' WHERE choice_id = '1424' AND question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1506' WHERE choice_id = '1424' AND question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1705' WHERE choice_id = '1425' AND question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1705' WHERE choice_id = '1425' AND question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1900' WHERE choice_id = '1426' AND question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1900' WHERE choice_id = '1426' AND question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2061' WHERE choice_id = '1427' AND question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2061' WHERE choice_id = '1427' AND question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2193' WHERE choice_id = '1428' AND question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2193' WHERE choice_id = '1428' AND question_id = '20177' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 40051
UPDATE consultation_results SET choice_id = '168' WHERE choice_id = '1' AND question_id = '40051' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '168' WHERE choice_id = '1' AND question_id = '40051' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '193' WHERE choice_id = '2' AND question_id = '40051' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '193' WHERE choice_id = '2' AND question_id = '40051' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '232' WHERE choice_id = '3' AND question_id = '40051' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '232' WHERE choice_id = '3' AND question_id = '40051' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '218' WHERE choice_id = '4' AND question_id = '40051' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '218' WHERE choice_id = '4' AND question_id = '40051' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 40052
UPDATE consultation_results SET choice_id = '169' WHERE choice_id = '5' AND question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '169' WHERE choice_id = '5' AND question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '194' WHERE choice_id = '6' AND question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '194' WHERE choice_id = '6' AND question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '219' WHERE choice_id = '7' AND question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '219' WHERE choice_id = '7' AND question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '244' WHERE choice_id = '9' AND question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '244' WHERE choice_id = '9' AND question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '233' WHERE choice_id = '8' AND question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '233' WHERE choice_id = '8' AND question_id = '40052' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 40055
UPDATE consultation_results SET choice_id = '172' WHERE choice_id = '10' AND question_id = '40055' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '172' WHERE choice_id = '10' AND question_id = '40055' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '197' WHERE choice_id = '11' AND question_id = '40055' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '197' WHERE choice_id = '11' AND question_id = '40055' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 40058
UPDATE consultation_results SET choice_id = '175' WHERE choice_id = '17' AND question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '175' WHERE choice_id = '17' AND question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '200' WHERE choice_id = '18' AND question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '200' WHERE choice_id = '18' AND question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '223' WHERE choice_id = '19' AND question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '223' WHERE choice_id = '19' AND question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '236' WHERE choice_id = '20' AND question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '236' WHERE choice_id = '20' AND question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '246' WHERE choice_id = '21' AND question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '246' WHERE choice_id = '21' AND question_id = '40058' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 40059
UPDATE consultation_results SET choice_id = '176' WHERE choice_id = '22' AND question_id = '40059' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '176' WHERE choice_id = '22' AND question_id = '40059' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '201' WHERE choice_id = '23' AND question_id = '40059' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '201' WHERE choice_id = '23' AND question_id = '40059' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 40060
UPDATE consultation_results SET choice_id = '177' WHERE choice_id = '24' AND question_id = '40060' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '177' WHERE choice_id = '24' AND question_id = '40060' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '202' WHERE choice_id = '25' AND question_id = '40060' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '202' WHERE choice_id = '25' AND question_id = '40060' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation dy9l5rzk9r3ocurra7xanvi7 / question 30173
UPDATE consultation_results SET choice_id = '2086' WHERE choice_id = '26' AND question_id = '30173' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '2086' WHERE choice_id = '26' AND question_id = '30173' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '1536' WHERE choice_id = '27' AND question_id = '30173' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '1536' WHERE choice_id = '27' AND question_id = '30173' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';

-- Consultation dy9l5rzk9r3ocurra7xanvi7 / question 30174
UPDATE consultation_results SET choice_id = '1537' WHERE choice_id = '28' AND question_id = '30174' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '1537' WHERE choice_id = '28' AND question_id = '30174' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '1736' WHERE choice_id = '29' AND question_id = '30174' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '1736' WHERE choice_id = '29' AND question_id = '30174' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '1930' WHERE choice_id = '30' AND question_id = '30174' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '1930' WHERE choice_id = '30' AND question_id = '30174' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 40047
UPDATE consultation_results SET choice_id = '164' WHERE choice_id = '38' AND question_id = '40047' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '164' WHERE choice_id = '38' AND question_id = '40047' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '214' WHERE choice_id = '40' AND question_id = '40047' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '214' WHERE choice_id = '40' AND question_id = '40047' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '229' WHERE choice_id = '41' AND question_id = '40047' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '229' WHERE choice_id = '41' AND question_id = '40047' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '189' WHERE choice_id = '39' AND question_id = '40047' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '189' WHERE choice_id = '39' AND question_id = '40047' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation dy9l5rzk9r3ocurra7xanvi7 / question 30175
UPDATE consultation_results SET choice_id = '2469' WHERE choice_id = '56' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '2469' WHERE choice_id = '56' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '2471' WHERE choice_id = '57' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '2471' WHERE choice_id = '57' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '2472' WHERE choice_id = '58' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '2472' WHERE choice_id = '58' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '2473' WHERE choice_id = '59' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '2473' WHERE choice_id = '59' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '2474' WHERE choice_id = '60' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '2474' WHERE choice_id = '60' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '2475' WHERE choice_id = '61' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '2475' WHERE choice_id = '61' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '2476' WHERE choice_id = '62' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '2476' WHERE choice_id = '62' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '2477' WHERE choice_id = '63' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '2477' WHERE choice_id = '63' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '2478' WHERE choice_id = '64' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '2478' WHERE choice_id = '64' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '2088' WHERE choice_id = '65' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '2088' WHERE choice_id = '65' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '2208' WHERE choice_id = '66' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '2208' WHERE choice_id = '66' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '2305' WHERE choice_id = '67' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '2305' WHERE choice_id = '67' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '2368' WHERE choice_id = '68' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '2368' WHERE choice_id = '68' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '1738' WHERE choice_id = '69' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '1738' WHERE choice_id = '69' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '1539' WHERE choice_id = '70' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '1539' WHERE choice_id = '70' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE consultation_results SET choice_id = '1932' WHERE choice_id = '71' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';
UPDATE reponses_consultation SET choice_id = '1932' WHERE choice_id = '71' AND question_id = '30175' AND consultation_id = 'dy9l5rzk9r3ocurra7xanvi7';

-- Consultation pwr61j5qhpm6ekrf3liuw5bc / question 30131
UPDATE consultation_results SET choice_id = '1846' WHERE choice_id = '72' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '1846' WHERE choice_id = '72' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '1448' WHERE choice_id = '73' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '1448' WHERE choice_id = '73' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '2155' WHERE choice_id = '74' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '2155' WHERE choice_id = '74' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '2015' WHERE choice_id = '75' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '2015' WHERE choice_id = '75' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '1647' WHERE choice_id = '76' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '1647' WHERE choice_id = '76' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE consultation_results SET choice_id = '2260' WHERE choice_id = '77' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';
UPDATE reponses_consultation SET choice_id = '2260' WHERE choice_id = '77' AND question_id = '30131' AND consultation_id = 'pwr61j5qhpm6ekrf3liuw5bc';

-- Consultation edujpj0n68x5n4ro2tpaxqr0 / question 40062
UPDATE consultation_results SET choice_id = '179' WHERE choice_id = '85' AND question_id = '40062' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '179' WHERE choice_id = '85' AND question_id = '40062' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE consultation_results SET choice_id = '204' WHERE choice_id = '86' AND question_id = '40062' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';
UPDATE reponses_consultation SET choice_id = '204' WHERE choice_id = '86' AND question_id = '40062' AND consultation_id = 'edujpj0n68x5n4ro2tpaxqr0';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 30134
UPDATE consultation_results SET choice_id = '1455' WHERE choice_id = '112' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1455' WHERE choice_id = '112' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2339' WHERE choice_id = '113' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2339' WHERE choice_id = '113' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1853' WHERE choice_id = '114' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1853' WHERE choice_id = '114' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1654' WHERE choice_id = '115' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1654' WHERE choice_id = '115' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2021' WHERE choice_id = '116' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2021' WHERE choice_id = '116' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2160' WHERE choice_id = '117' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2160' WHERE choice_id = '117' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2265' WHERE choice_id = '118' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2265' WHERE choice_id = '118' AND question_id = '30134' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 30135
UPDATE consultation_results SET choice_id = '1854' WHERE choice_id = '119' AND question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1854' WHERE choice_id = '119' AND question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1456' WHERE choice_id = '120' AND question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1456' WHERE choice_id = '120' AND question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1655' WHERE choice_id = '121' AND question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1655' WHERE choice_id = '121' AND question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2022' WHERE choice_id = '122' AND question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2022' WHERE choice_id = '122' AND question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2161' WHERE choice_id = '123' AND question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2161' WHERE choice_id = '123' AND question_id = '30135' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 30136
UPDATE consultation_results SET choice_id = '1458' WHERE choice_id = '138' AND question_id = '30136' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1458' WHERE choice_id = '138' AND question_id = '30136' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1657' WHERE choice_id = '139' AND question_id = '30136' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1657' WHERE choice_id = '139' AND question_id = '30136' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '2024' WHERE choice_id = '140' AND question_id = '30136' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '2024' WHERE choice_id = '140' AND question_id = '30136' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1856' WHERE choice_id = '141' AND question_id = '30136' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1856' WHERE choice_id = '141' AND question_id = '30136' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 30138
UPDATE consultation_results SET choice_id = '1663' WHERE choice_id = '174' AND question_id = '30138' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1663' WHERE choice_id = '174' AND question_id = '30138' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1464' WHERE choice_id = '175' AND question_id = '30138' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1464' WHERE choice_id = '175' AND question_id = '30138' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1862' WHERE choice_id = '176' AND question_id = '30138' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1862' WHERE choice_id = '176' AND question_id = '30138' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 30139
UPDATE consultation_results SET choice_id = '1466' WHERE choice_id = '182' AND question_id = '30139' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1466' WHERE choice_id = '182' AND question_id = '30139' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1665' WHERE choice_id = '184' AND question_id = '30139' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1665' WHERE choice_id = '184' AND question_id = '30139' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation cmgxi3u8u7oc67qlwsiw7wk2 / question 30137
UPDATE consultation_results SET choice_id = '1662' WHERE choice_id = '171' AND question_id = '30137' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1662' WHERE choice_id = '171' AND question_id = '30137' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1861' WHERE choice_id = '172' AND question_id = '30137' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1861' WHERE choice_id = '172' AND question_id = '30137' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE consultation_results SET choice_id = '1463' WHERE choice_id = '173' AND question_id = '30137' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';
UPDATE reponses_consultation SET choice_id = '1463' WHERE choice_id = '173' AND question_id = '30137' AND consultation_id = 'cmgxi3u8u7oc67qlwsiw7wk2';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 30142
UPDATE consultation_results SET choice_id = '1476' WHERE choice_id = '200' AND question_id = '30142' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1476' WHERE choice_id = '200' AND question_id = '30142' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1675' WHERE choice_id = '201' AND question_id = '30142' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1675' WHERE choice_id = '201' AND question_id = '30142' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1873' WHERE choice_id = '202' AND question_id = '30142' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1873' WHERE choice_id = '202' AND question_id = '30142' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '2039' WHERE choice_id = '203' AND question_id = '30142' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '2039' WHERE choice_id = '203' AND question_id = '30142' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 30143
UPDATE consultation_results SET choice_id = '1479' WHERE choice_id = '214' AND question_id = '30143' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1479' WHERE choice_id = '214' AND question_id = '30143' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1678' WHERE choice_id = '215' AND question_id = '30143' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1678' WHERE choice_id = '215' AND question_id = '30143' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1876' WHERE choice_id = '216' AND question_id = '30143' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1876' WHERE choice_id = '216' AND question_id = '30143' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 30144
UPDATE consultation_results SET choice_id = '1480' WHERE choice_id = '217' AND question_id = '30144' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1480' WHERE choice_id = '217' AND question_id = '30144' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1877' WHERE choice_id = '218' AND question_id = '30144' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1877' WHERE choice_id = '218' AND question_id = '30144' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1679' WHERE choice_id = '219' AND question_id = '30144' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1679' WHERE choice_id = '219' AND question_id = '30144' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 30145
UPDATE consultation_results SET choice_id = '1482' WHERE choice_id = '227' AND question_id = '30145' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1482' WHERE choice_id = '227' AND question_id = '30145' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1681' WHERE choice_id = '228' AND question_id = '30145' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1681' WHERE choice_id = '228' AND question_id = '30145' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 30150
UPDATE consultation_results SET choice_id = '1497' WHERE choice_id = '242' AND question_id = '30150' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1497' WHERE choice_id = '242' AND question_id = '30150' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1696' WHERE choice_id = '243' AND question_id = '30150' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1696' WHERE choice_id = '243' AND question_id = '30150' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 30151
UPDATE consultation_results SET choice_id = '1697' WHERE choice_id = '244' AND question_id = '30151' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1697' WHERE choice_id = '244' AND question_id = '30151' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1893' WHERE choice_id = '245' AND question_id = '30151' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1893' WHERE choice_id = '245' AND question_id = '30151' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1498' WHERE choice_id = '246' AND question_id = '30151' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1498' WHERE choice_id = '246' AND question_id = '30151' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 30153
UPDATE consultation_results SET choice_id = '1500' WHERE choice_id = '248' AND question_id = '30153' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1500' WHERE choice_id = '248' AND question_id = '30153' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1699' WHERE choice_id = '250' AND question_id = '30153' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1699' WHERE choice_id = '250' AND question_id = '30153' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation vciluo7jt2hiavx7wuqbfwh1 / question 30152
UPDATE consultation_results SET choice_id = '1499' WHERE choice_id = '247' AND question_id = '30152' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1499' WHERE choice_id = '247' AND question_id = '30152' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1894' WHERE choice_id = '249' AND question_id = '30152' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1894' WHERE choice_id = '249' AND question_id = '30152' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE consultation_results SET choice_id = '1698' WHERE choice_id = '251' AND question_id = '30152' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';
UPDATE reponses_consultation SET choice_id = '1698' WHERE choice_id = '251' AND question_id = '30152' AND consultation_id = 'vciluo7jt2hiavx7wuqbfwh1';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 30123
UPDATE consultation_results SET choice_id = '1431' WHERE choice_id = '264' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1431' WHERE choice_id = '264' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1630' WHERE choice_id = '265' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1630' WHERE choice_id = '265' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1829' WHERE choice_id = '266' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1829' WHERE choice_id = '266' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2144' WHERE choice_id = '267' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2144' WHERE choice_id = '267' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2003' WHERE choice_id = '268' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2003' WHERE choice_id = '268' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2252' WHERE choice_id = '269' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2252' WHERE choice_id = '269' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2330' WHERE choice_id = '270' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2330' WHERE choice_id = '270' AND question_id = '30123' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 30124
UPDATE consultation_results SET choice_id = '1830' WHERE choice_id = '271' AND question_id = '30124' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1830' WHERE choice_id = '271' AND question_id = '30124' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1432' WHERE choice_id = '272' AND question_id = '30124' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1432' WHERE choice_id = '272' AND question_id = '30124' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1631' WHERE choice_id = '273' AND question_id = '30124' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1631' WHERE choice_id = '273' AND question_id = '30124' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 30127
UPDATE consultation_results SET choice_id = '1441' WHERE choice_id = '302' AND question_id = '30127' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1441' WHERE choice_id = '302' AND question_id = '30127' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1640' WHERE choice_id = '303' AND question_id = '30127' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1640' WHERE choice_id = '303' AND question_id = '30127' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1839' WHERE choice_id = '304' AND question_id = '30127' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1839' WHERE choice_id = '304' AND question_id = '30127' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 30129
UPDATE consultation_results SET choice_id = '1443' WHERE choice_id = '308' AND question_id = '30129' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1443' WHERE choice_id = '308' AND question_id = '30129' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1642' WHERE choice_id = '309' AND question_id = '30129' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1642' WHERE choice_id = '309' AND question_id = '30129' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1841' WHERE choice_id = '310' AND question_id = '30129' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1841' WHERE choice_id = '310' AND question_id = '30129' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 30128
UPDATE consultation_results SET choice_id = '1442' WHERE choice_id = '305' AND question_id = '30128' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1442' WHERE choice_id = '305' AND question_id = '30128' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1641' WHERE choice_id = '306' AND question_id = '30128' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1641' WHERE choice_id = '306' AND question_id = '30128' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1840' WHERE choice_id = '307' AND question_id = '30128' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1840' WHERE choice_id = '307' AND question_id = '30128' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation pk4qi9rq5v6nuu2t39cck3mo / question 30130
UPDATE consultation_results SET choice_id = '1446' WHERE choice_id = '332' AND question_id = '30130' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1446' WHERE choice_id = '332' AND question_id = '30130' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1645' WHERE choice_id = '333' AND question_id = '30130' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1645' WHERE choice_id = '333' AND question_id = '30130' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '1844' WHERE choice_id = '334' AND question_id = '30130' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '1844' WHERE choice_id = '334' AND question_id = '30130' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE consultation_results SET choice_id = '2013' WHERE choice_id = '335' AND question_id = '30130' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';
UPDATE reponses_consultation SET choice_id = '2013' WHERE choice_id = '335' AND question_id = '30130' AND consultation_id = 'pk4qi9rq5v6nuu2t39cck3mo';

-- Consultation kmbc0ie5zsx94dejoiv1wcnv / question 30146
UPDATE consultation_results SET choice_id = '1484' WHERE choice_id = '336' AND question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1484' WHERE choice_id = '336' AND question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '1683' WHERE choice_id = '337' AND question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1683' WHERE choice_id = '337' AND question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2044' WHERE choice_id = '338' AND question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2044' WHERE choice_id = '338' AND question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2179' WHERE choice_id = '339' AND question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2179' WHERE choice_id = '339' AND question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '1880' WHERE choice_id = '340' AND question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1880' WHERE choice_id = '340' AND question_id = '30146' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation kmbc0ie5zsx94dejoiv1wcnv / question 30148
UPDATE consultation_results SET choice_id = '1685' WHERE choice_id = '345' AND question_id = '30148' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1685' WHERE choice_id = '345' AND question_id = '30148' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '1486' WHERE choice_id = '346' AND question_id = '30148' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1486' WHERE choice_id = '346' AND question_id = '30148' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '1882' WHERE choice_id = '347' AND question_id = '30148' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1882' WHERE choice_id = '347' AND question_id = '30148' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2046' WHERE choice_id = '350' AND question_id = '30148' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2046' WHERE choice_id = '350' AND question_id = '30148' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation kmbc0ie5zsx94dejoiv1wcnv / question 30147
UPDATE consultation_results SET choice_id = '1684' WHERE choice_id = '341' AND question_id = '30147' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1684' WHERE choice_id = '341' AND question_id = '30147' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '1881' WHERE choice_id = '342' AND question_id = '30147' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1881' WHERE choice_id = '342' AND question_id = '30147' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2045' WHERE choice_id = '343' AND question_id = '30147' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2045' WHERE choice_id = '343' AND question_id = '30147' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '1485' WHERE choice_id = '344' AND question_id = '30147' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1485' WHERE choice_id = '344' AND question_id = '30147' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation kmbc0ie5zsx94dejoiv1wcnv / question 30149
UPDATE consultation_results SET choice_id = '2287' WHERE choice_id = '387' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2287' WHERE choice_id = '387' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '1691' WHERE choice_id = '389' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1691' WHERE choice_id = '389' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2185' WHERE choice_id = '391' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2185' WHERE choice_id = '391' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '1888' WHERE choice_id = '392' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1888' WHERE choice_id = '392' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '2052' WHERE choice_id = '393' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '2052' WHERE choice_id = '393' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE consultation_results SET choice_id = '1492' WHERE choice_id = '394' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';
UPDATE reponses_consultation SET choice_id = '1492' WHERE choice_id = '394' AND question_id = '30149' AND consultation_id = 'kmbc0ie5zsx94dejoiv1wcnv';

-- Consultation w5d0rrhdlqby9shpt4wdk5oj / question 30140
UPDATE consultation_results SET choice_id = '1667' WHERE choice_id = '404' AND question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '1667' WHERE choice_id = '404' AND question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '1468' WHERE choice_id = '405' AND question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '1468' WHERE choice_id = '405' AND question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2169' WHERE choice_id = '406' AND question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2169' WHERE choice_id = '406' AND question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2031' WHERE choice_id = '407' AND question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2031' WHERE choice_id = '407' AND question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '1865' WHERE choice_id = '408' AND question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '1865' WHERE choice_id = '408' AND question_id = '30140' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';

-- Consultation w5d0rrhdlqby9shpt4wdk5oj / question 30141
UPDATE consultation_results SET choice_id = '1469' WHERE choice_id = '409' AND question_id = '30141' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '1469' WHERE choice_id = '409' AND question_id = '30141' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '2032' WHERE choice_id = '411' AND question_id = '30141' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '2032' WHERE choice_id = '411' AND question_id = '30141' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '1668' WHERE choice_id = '412' AND question_id = '30141' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '1668' WHERE choice_id = '412' AND question_id = '30141' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE consultation_results SET choice_id = '1866' WHERE choice_id = '415' AND question_id = '30141' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';
UPDATE reponses_consultation SET choice_id = '1866' WHERE choice_id = '415' AND question_id = '30141' AND consultation_id = 'w5d0rrhdlqby9shpt4wdk5oj';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30166
UPDATE consultation_results SET choice_id = '1919' WHERE choice_id = '443' AND question_id = '30166' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1919' WHERE choice_id = '443' AND question_id = '30166' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1526' WHERE choice_id = '444' AND question_id = '30166' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1526' WHERE choice_id = '444' AND question_id = '30166' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1725' WHERE choice_id = '445' AND question_id = '30166' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1725' WHERE choice_id = '445' AND question_id = '30166' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30167
UPDATE consultation_results SET choice_id = '1527' WHERE choice_id = '446' AND question_id = '30167' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1527' WHERE choice_id = '446' AND question_id = '30167' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1726' WHERE choice_id = '447' AND question_id = '30167' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1726' WHERE choice_id = '447' AND question_id = '30167' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1920' WHERE choice_id = '448' AND question_id = '30167' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1920' WHERE choice_id = '448' AND question_id = '30167' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2078' WHERE choice_id = '449' AND question_id = '30167' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2078' WHERE choice_id = '449' AND question_id = '30167' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30180
UPDATE consultation_results SET choice_id = '1548' WHERE choice_id = '457' AND question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1548' WHERE choice_id = '457' AND question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1747' WHERE choice_id = '458' AND question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1747' WHERE choice_id = '458' AND question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1941' WHERE choice_id = '459' AND question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1941' WHERE choice_id = '459' AND question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2095' WHERE choice_id = '460' AND question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2095' WHERE choice_id = '460' AND question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2213' WHERE choice_id = '461' AND question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2213' WHERE choice_id = '461' AND question_id = '30180' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30179
UPDATE consultation_results SET choice_id = '1547' WHERE choice_id = '453' AND question_id = '30179' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1547' WHERE choice_id = '453' AND question_id = '30179' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1746' WHERE choice_id = '454' AND question_id = '30179' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1746' WHERE choice_id = '454' AND question_id = '30179' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1940' WHERE choice_id = '456' AND question_id = '30179' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1940' WHERE choice_id = '456' AND question_id = '30179' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30178
UPDATE consultation_results SET choice_id = '1546' WHERE choice_id = '450' AND question_id = '30178' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1546' WHERE choice_id = '450' AND question_id = '30178' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1745' WHERE choice_id = '451' AND question_id = '30178' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1745' WHERE choice_id = '451' AND question_id = '30178' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1939' WHERE choice_id = '452' AND question_id = '30178' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1939' WHERE choice_id = '452' AND question_id = '30178' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2094' WHERE choice_id = '455' AND question_id = '30178' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2094' WHERE choice_id = '455' AND question_id = '30178' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30182
UPDATE consultation_results SET choice_id = '1550' WHERE choice_id = '464' AND question_id = '30182' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1550' WHERE choice_id = '464' AND question_id = '30182' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1749' WHERE choice_id = '465' AND question_id = '30182' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1749' WHERE choice_id = '465' AND question_id = '30182' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30181
UPDATE consultation_results SET choice_id = '1549' WHERE choice_id = '462' AND question_id = '30181' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1549' WHERE choice_id = '462' AND question_id = '30181' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1748' WHERE choice_id = '463' AND question_id = '30181' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1748' WHERE choice_id = '463' AND question_id = '30181' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1942' WHERE choice_id = '466' AND question_id = '30181' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1942' WHERE choice_id = '466' AND question_id = '30181' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30183
UPDATE consultation_results SET choice_id = '1750' WHERE choice_id = '467' AND question_id = '30183' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1750' WHERE choice_id = '467' AND question_id = '30183' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1551' WHERE choice_id = '468' AND question_id = '30183' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1551' WHERE choice_id = '468' AND question_id = '30183' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30186
UPDATE consultation_results SET choice_id = '1754' WHERE choice_id = '476' AND question_id = '30186' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1754' WHERE choice_id = '476' AND question_id = '30186' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1555' WHERE choice_id = '477' AND question_id = '30186' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1555' WHERE choice_id = '477' AND question_id = '30186' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30185
UPDATE consultation_results SET choice_id = '1752' WHERE choice_id = '472' AND question_id = '30185' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1752' WHERE choice_id = '472' AND question_id = '30185' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1553' WHERE choice_id = '473' AND question_id = '30185' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1553' WHERE choice_id = '473' AND question_id = '30185' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1944' WHERE choice_id = '474' AND question_id = '30185' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1944' WHERE choice_id = '474' AND question_id = '30185' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2096' WHERE choice_id = '475' AND question_id = '30185' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2096' WHERE choice_id = '475' AND question_id = '30185' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30188
UPDATE consultation_results SET choice_id = '1557' WHERE choice_id = '480' AND question_id = '30188' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1557' WHERE choice_id = '480' AND question_id = '30188' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1756' WHERE choice_id = '481' AND question_id = '30188' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1756' WHERE choice_id = '481' AND question_id = '30188' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1946' WHERE choice_id = '482' AND question_id = '30188' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1946' WHERE choice_id = '482' AND question_id = '30188' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2098' WHERE choice_id = '483' AND question_id = '30188' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2098' WHERE choice_id = '483' AND question_id = '30188' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30184
UPDATE consultation_results SET choice_id = '1751' WHERE choice_id = '469' AND question_id = '30184' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1751' WHERE choice_id = '469' AND question_id = '30184' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1552' WHERE choice_id = '470' AND question_id = '30184' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1552' WHERE choice_id = '470' AND question_id = '30184' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1943' WHERE choice_id = '471' AND question_id = '30184' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1943' WHERE choice_id = '471' AND question_id = '30184' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30187
UPDATE consultation_results SET choice_id = '1755' WHERE choice_id = '478' AND question_id = '30187' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1755' WHERE choice_id = '478' AND question_id = '30187' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1556' WHERE choice_id = '479' AND question_id = '30187' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1556' WHERE choice_id = '479' AND question_id = '30187' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30189
UPDATE consultation_results SET choice_id = '1559' WHERE choice_id = '493' AND question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1559' WHERE choice_id = '493' AND question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1948' WHERE choice_id = '494' AND question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1948' WHERE choice_id = '494' AND question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2216' WHERE choice_id = '495' AND question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2216' WHERE choice_id = '495' AND question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1758' WHERE choice_id = '496' AND question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1758' WHERE choice_id = '496' AND question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2100' WHERE choice_id = '497' AND question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2100' WHERE choice_id = '497' AND question_id = '30189' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30190
UPDATE consultation_results SET choice_id = '1560' WHERE choice_id = '499' AND question_id = '30190' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1560' WHERE choice_id = '499' AND question_id = '30190' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1949' WHERE choice_id = '500' AND question_id = '30190' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1949' WHERE choice_id = '500' AND question_id = '30190' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1759' WHERE choice_id = '502' AND question_id = '30190' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1759' WHERE choice_id = '502' AND question_id = '30190' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30191
UPDATE consultation_results SET choice_id = '1760' WHERE choice_id = '498' AND question_id = '30191' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1760' WHERE choice_id = '498' AND question_id = '30191' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1950' WHERE choice_id = '501' AND question_id = '30191' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1950' WHERE choice_id = '501' AND question_id = '30191' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2101' WHERE choice_id = '503' AND question_id = '30191' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2101' WHERE choice_id = '503' AND question_id = '30191' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1561' WHERE choice_id = '504' AND question_id = '30191' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1561' WHERE choice_id = '504' AND question_id = '30191' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30193
UPDATE consultation_results SET choice_id = '1563' WHERE choice_id = '507' AND question_id = '30193' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1563' WHERE choice_id = '507' AND question_id = '30193' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1762' WHERE choice_id = '508' AND question_id = '30193' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1762' WHERE choice_id = '508' AND question_id = '30193' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30192
UPDATE consultation_results SET choice_id = '1562' WHERE choice_id = '505' AND question_id = '30192' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1562' WHERE choice_id = '505' AND question_id = '30192' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1761' WHERE choice_id = '506' AND question_id = '30192' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1761' WHERE choice_id = '506' AND question_id = '30192' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30194
UPDATE consultation_results SET choice_id = '1763' WHERE choice_id = '510' AND question_id = '30194' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1763' WHERE choice_id = '510' AND question_id = '30194' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1564' WHERE choice_id = '511' AND question_id = '30194' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1564' WHERE choice_id = '511' AND question_id = '30194' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30197
UPDATE consultation_results SET choice_id = '1568' WHERE choice_id = '522' AND question_id = '30197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1568' WHERE choice_id = '522' AND question_id = '30197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1767' WHERE choice_id = '523' AND question_id = '30197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1767' WHERE choice_id = '523' AND question_id = '30197' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30195
UPDATE consultation_results SET choice_id = '1566' WHERE choice_id = '513' AND question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1566' WHERE choice_id = '513' AND question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2103' WHERE choice_id = '514' AND question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2103' WHERE choice_id = '514' AND question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1952' WHERE choice_id = '517' AND question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1952' WHERE choice_id = '517' AND question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2217' WHERE choice_id = '518' AND question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2217' WHERE choice_id = '518' AND question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1765' WHERE choice_id = '519' AND question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1765' WHERE choice_id = '519' AND question_id = '30195' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30198
UPDATE consultation_results SET choice_id = '2218' WHERE choice_id = '524' AND question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2218' WHERE choice_id = '524' AND question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2104' WHERE choice_id = '525' AND question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2104' WHERE choice_id = '525' AND question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1953' WHERE choice_id = '526' AND question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1953' WHERE choice_id = '526' AND question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1768' WHERE choice_id = '527' AND question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1768' WHERE choice_id = '527' AND question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1569' WHERE choice_id = '528' AND question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1569' WHERE choice_id = '528' AND question_id = '30198' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30196
UPDATE consultation_results SET choice_id = '1567' WHERE choice_id = '520' AND question_id = '30196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1567' WHERE choice_id = '520' AND question_id = '30196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1766' WHERE choice_id = '521' AND question_id = '30196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1766' WHERE choice_id = '521' AND question_id = '30196' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30199
UPDATE consultation_results SET choice_id = '1570' WHERE choice_id = '529' AND question_id = '30199' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1570' WHERE choice_id = '529' AND question_id = '30199' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1769' WHERE choice_id = '530' AND question_id = '30199' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1769' WHERE choice_id = '530' AND question_id = '30199' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1954' WHERE choice_id = '531' AND question_id = '30199' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1954' WHERE choice_id = '531' AND question_id = '30199' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30204
UPDATE consultation_results SET choice_id = '1586' WHERE choice_id = '535' AND question_id = '30204' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1586' WHERE choice_id = '535' AND question_id = '30204' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1785' WHERE choice_id = '540' AND question_id = '30204' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1785' WHERE choice_id = '540' AND question_id = '30204' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30201
UPDATE consultation_results SET choice_id = '1771' WHERE choice_id = '536' AND question_id = '30201' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1771' WHERE choice_id = '536' AND question_id = '30201' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1572' WHERE choice_id = '537' AND question_id = '30201' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1572' WHERE choice_id = '537' AND question_id = '30201' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30200
UPDATE consultation_results SET choice_id = '1770' WHERE choice_id = '532' AND question_id = '30200' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1770' WHERE choice_id = '532' AND question_id = '30200' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1571' WHERE choice_id = '533' AND question_id = '30200' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1571' WHERE choice_id = '533' AND question_id = '30200' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1955' WHERE choice_id = '534' AND question_id = '30200' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1955' WHERE choice_id = '534' AND question_id = '30200' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2105' WHERE choice_id = '538' AND question_id = '30200' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2105' WHERE choice_id = '538' AND question_id = '30200' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30208
UPDATE consultation_results SET choice_id = '1592' WHERE choice_id = '562' AND question_id = '30208' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1592' WHERE choice_id = '562' AND question_id = '30208' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1791' WHERE choice_id = '563' AND question_id = '30208' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1791' WHERE choice_id = '563' AND question_id = '30208' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1973' WHERE choice_id = '564' AND question_id = '30208' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1973' WHERE choice_id = '564' AND question_id = '30208' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30205
UPDATE consultation_results SET choice_id = '1788' WHERE choice_id = '552' AND question_id = '30205' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1788' WHERE choice_id = '552' AND question_id = '30205' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1589' WHERE choice_id = '553' AND question_id = '30205' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1589' WHERE choice_id = '553' AND question_id = '30205' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30206
UPDATE consultation_results SET choice_id = '2121' WHERE choice_id = '554' AND question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2121' WHERE choice_id = '554' AND question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1971' WHERE choice_id = '555' AND question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1971' WHERE choice_id = '555' AND question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '2233' WHERE choice_id = '556' AND question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '2233' WHERE choice_id = '556' AND question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1789' WHERE choice_id = '557' AND question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1789' WHERE choice_id = '557' AND question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1590' WHERE choice_id = '558' AND question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1590' WHERE choice_id = '558' AND question_id = '30206' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30207
UPDATE consultation_results SET choice_id = '1591' WHERE choice_id = '559' AND question_id = '30207' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1591' WHERE choice_id = '559' AND question_id = '30207' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1790' WHERE choice_id = '560' AND question_id = '30207' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1790' WHERE choice_id = '560' AND question_id = '30207' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1972' WHERE choice_id = '561' AND question_id = '30207' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1972' WHERE choice_id = '561' AND question_id = '30207' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30209
UPDATE consultation_results SET choice_id = '1593' WHERE choice_id = '565' AND question_id = '30209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1593' WHERE choice_id = '565' AND question_id = '30209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1792' WHERE choice_id = '566' AND question_id = '30209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1792' WHERE choice_id = '566' AND question_id = '30209' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30210
UPDATE consultation_results SET choice_id = '1594' WHERE choice_id = '567' AND question_id = '30210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1594' WHERE choice_id = '567' AND question_id = '30210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1793' WHERE choice_id = '569' AND question_id = '30210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1793' WHERE choice_id = '569' AND question_id = '30210' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation wv0ul09acpw4f38v6s4uudqn / question 30211
UPDATE consultation_results SET choice_id = '1595' WHERE choice_id = '568' AND question_id = '30211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1595' WHERE choice_id = '568' AND question_id = '30211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE consultation_results SET choice_id = '1794' WHERE choice_id = '570' AND question_id = '30211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';
UPDATE reponses_consultation SET choice_id = '1794' WHERE choice_id = '570' AND question_id = '30211' AND consultation_id = 'wv0ul09acpw4f38v6s4uudqn';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 30160
UPDATE consultation_results SET choice_id = '1513' WHERE choice_id = '584' AND question_id = '30160' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1513' WHERE choice_id = '584' AND question_id = '30160' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1712' WHERE choice_id = '585' AND question_id = '30160' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1712' WHERE choice_id = '585' AND question_id = '30160' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1906' WHERE choice_id = '586' AND question_id = '30160' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1906' WHERE choice_id = '586' AND question_id = '30160' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2066' WHERE choice_id = '587' AND question_id = '30160' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2066' WHERE choice_id = '587' AND question_id = '30160' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 30158
UPDATE consultation_results SET choice_id = '1710' WHERE choice_id = '577' AND question_id = '30158' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1710' WHERE choice_id = '577' AND question_id = '30158' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1511' WHERE choice_id = '578' AND question_id = '30158' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1511' WHERE choice_id = '578' AND question_id = '30158' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1904' WHERE choice_id = '579' AND question_id = '30158' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1904' WHERE choice_id = '579' AND question_id = '30158' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 30157
UPDATE consultation_results SET choice_id = '1510' WHERE choice_id = '575' AND question_id = '30157' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1510' WHERE choice_id = '575' AND question_id = '30157' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1709' WHERE choice_id = '576' AND question_id = '30157' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1709' WHERE choice_id = '576' AND question_id = '30157' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 30159
UPDATE consultation_results SET choice_id = '1711' WHERE choice_id = '580' AND question_id = '30159' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1711' WHERE choice_id = '580' AND question_id = '30159' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1512' WHERE choice_id = '581' AND question_id = '30159' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1512' WHERE choice_id = '581' AND question_id = '30159' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1905' WHERE choice_id = '582' AND question_id = '30159' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1905' WHERE choice_id = '582' AND question_id = '30159' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2065' WHERE choice_id = '583' AND question_id = '30159' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2065' WHERE choice_id = '583' AND question_id = '30159' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 30161
UPDATE consultation_results SET choice_id = '1515' WHERE choice_id = '594' AND question_id = '30161' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1515' WHERE choice_id = '594' AND question_id = '30161' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1714' WHERE choice_id = '595' AND question_id = '30161' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1714' WHERE choice_id = '595' AND question_id = '30161' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1908' WHERE choice_id = '596' AND question_id = '30161' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1908' WHERE choice_id = '596' AND question_id = '30161' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 30162
UPDATE consultation_results SET choice_id = '1715' WHERE choice_id = '597' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1715' WHERE choice_id = '597' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1516' WHERE choice_id = '598' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1516' WHERE choice_id = '598' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1909' WHERE choice_id = '599' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1909' WHERE choice_id = '599' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2296' WHERE choice_id = '600' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2296' WHERE choice_id = '600' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2068' WHERE choice_id = '601' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2068' WHERE choice_id = '601' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2197' WHERE choice_id = '602' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2197' WHERE choice_id = '602' AND question_id = '30162' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 30164
UPDATE consultation_results SET choice_id = '1520' WHERE choice_id = '619' AND question_id = '30164' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1520' WHERE choice_id = '619' AND question_id = '30164' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2072' WHERE choice_id = '620' AND question_id = '30164' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2072' WHERE choice_id = '620' AND question_id = '30164' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1719' WHERE choice_id = '621' AND question_id = '30164' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1719' WHERE choice_id = '621' AND question_id = '30164' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1913' WHERE choice_id = '622' AND question_id = '30164' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1913' WHERE choice_id = '622' AND question_id = '30164' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 30165
UPDATE consultation_results SET choice_id = '1522' WHERE choice_id = '629' AND question_id = '30165' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1522' WHERE choice_id = '629' AND question_id = '30165' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1721' WHERE choice_id = '632' AND question_id = '30165' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1721' WHERE choice_id = '632' AND question_id = '30165' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2074' WHERE choice_id = '633' AND question_id = '30165' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2074' WHERE choice_id = '633' AND question_id = '30165' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1915' WHERE choice_id = '634' AND question_id = '30165' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1915' WHERE choice_id = '634' AND question_id = '30165' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation g8w8ivfieyvs3ajinnbk4015 / question 30163
UPDATE consultation_results SET choice_id = '1519' WHERE choice_id = '613' AND question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1519' WHERE choice_id = '613' AND question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1912' WHERE choice_id = '615' AND question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1912' WHERE choice_id = '615' AND question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2200' WHERE choice_id = '616' AND question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2200' WHERE choice_id = '616' AND question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '2071' WHERE choice_id = '617' AND question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '2071' WHERE choice_id = '617' AND question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE consultation_results SET choice_id = '1718' WHERE choice_id = '618' AND question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';
UPDATE reponses_consultation SET choice_id = '1718' WHERE choice_id = '618' AND question_id = '30163' AND consultation_id = 'g8w8ivfieyvs3ajinnbk4015';

-- Consultation vkp77x0ognt41xyjicz9wwba / question 30202
UPDATE consultation_results SET choice_id = '1573' WHERE choice_id = '635' AND question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '1573' WHERE choice_id = '635' AND question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '1772' WHERE choice_id = '636' AND question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '1772' WHERE choice_id = '636' AND question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '1956' WHERE choice_id = '637' AND question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '1956' WHERE choice_id = '637' AND question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '2106' WHERE choice_id = '638' AND question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '2106' WHERE choice_id = '638' AND question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '2219' WHERE choice_id = '639' AND question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '2219' WHERE choice_id = '639' AND question_id = '30202' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';

-- Consultation vkp77x0ognt41xyjicz9wwba / question 30203
UPDATE consultation_results SET choice_id = '1578' WHERE choice_id = '664' AND question_id = '30203' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '1578' WHERE choice_id = '664' AND question_id = '30203' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '2111' WHERE choice_id = '666' AND question_id = '30203' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '2111' WHERE choice_id = '666' AND question_id = '30203' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '1777' WHERE choice_id = '667' AND question_id = '30203' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '1777' WHERE choice_id = '667' AND question_id = '30203' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE consultation_results SET choice_id = '1961' WHERE choice_id = '668' AND question_id = '30203' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';
UPDATE reponses_consultation SET choice_id = '1961' WHERE choice_id = '668' AND question_id = '30203' AND consultation_id = 'vkp77x0ognt41xyjicz9wwba';

-- Consultation rn3ib6tpe4yv3l7ofolyqh7d / question 30215
UPDATE consultation_results SET choice_id = '1602' WHERE choice_id = '674' AND question_id = '30215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1602' WHERE choice_id = '674' AND question_id = '30215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '1801' WHERE choice_id = '675' AND question_id = '30215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1801' WHERE choice_id = '675' AND question_id = '30215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '1980' WHERE choice_id = '677' AND question_id = '30215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1980' WHERE choice_id = '677' AND question_id = '30215' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';

-- Consultation rn3ib6tpe4yv3l7ofolyqh7d / question 30217
UPDATE consultation_results SET choice_id = '1604' WHERE choice_id = '681' AND question_id = '30217' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1604' WHERE choice_id = '681' AND question_id = '30217' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '1803' WHERE choice_id = '682' AND question_id = '30217' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1803' WHERE choice_id = '682' AND question_id = '30217' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '1982' WHERE choice_id = '683' AND question_id = '30217' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1982' WHERE choice_id = '683' AND question_id = '30217' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';

-- Consultation rn3ib6tpe4yv3l7ofolyqh7d / question 30216
UPDATE consultation_results SET choice_id = '1603' WHERE choice_id = '676' AND question_id = '30216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1603' WHERE choice_id = '676' AND question_id = '30216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '1981' WHERE choice_id = '678' AND question_id = '30216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1981' WHERE choice_id = '678' AND question_id = '30216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '1802' WHERE choice_id = '679' AND question_id = '30216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '1802' WHERE choice_id = '679' AND question_id = '30216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE consultation_results SET choice_id = '2126' WHERE choice_id = '680' AND question_id = '30216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';
UPDATE reponses_consultation SET choice_id = '2126' WHERE choice_id = '680' AND question_id = '30216' AND consultation_id = 'rn3ib6tpe4yv3l7ofolyqh7d';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 30223
UPDATE consultation_results SET choice_id = '1616' WHERE choice_id = '730' AND question_id = '30223' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1616' WHERE choice_id = '730' AND question_id = '30223' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1815' WHERE choice_id = '732' AND question_id = '30223' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1815' WHERE choice_id = '732' AND question_id = '30223' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1991' WHERE choice_id = '733' AND question_id = '30223' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1991' WHERE choice_id = '733' AND question_id = '30223' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 30222
UPDATE consultation_results SET choice_id = '1814' WHERE choice_id = '728' AND question_id = '30222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1814' WHERE choice_id = '728' AND question_id = '30222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1615' WHERE choice_id = '729' AND question_id = '30222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1615' WHERE choice_id = '729' AND question_id = '30222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1990' WHERE choice_id = '731' AND question_id = '30222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1990' WHERE choice_id = '731' AND question_id = '30222' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 30221
UPDATE consultation_results SET choice_id = '1614' WHERE choice_id = '724' AND question_id = '30221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1614' WHERE choice_id = '724' AND question_id = '30221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1813' WHERE choice_id = '726' AND question_id = '30221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1813' WHERE choice_id = '726' AND question_id = '30221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1989' WHERE choice_id = '727' AND question_id = '30221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1989' WHERE choice_id = '727' AND question_id = '30221' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 30224
UPDATE consultation_results SET choice_id = '1617' WHERE choice_id = '734' AND question_id = '30224' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1617' WHERE choice_id = '734' AND question_id = '30224' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1816' WHERE choice_id = '735' AND question_id = '30224' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1816' WHERE choice_id = '735' AND question_id = '30224' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 30218
UPDATE consultation_results SET choice_id = '1807' WHERE choice_id = '763' AND question_id = '30218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1807' WHERE choice_id = '763' AND question_id = '30218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1608' WHERE choice_id = '765' AND question_id = '30218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1608' WHERE choice_id = '765' AND question_id = '30218' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 30219
UPDATE consultation_results SET choice_id = '1609' WHERE choice_id = '764' AND question_id = '30219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1609' WHERE choice_id = '764' AND question_id = '30219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1808' WHERE choice_id = '766' AND question_id = '30219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1808' WHERE choice_id = '766' AND question_id = '30219' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation ei80m02y5koialuq4e30n4q6 / question 30220
UPDATE consultation_results SET choice_id = '1809' WHERE choice_id = '767' AND question_id = '30220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1809' WHERE choice_id = '767' AND question_id = '30220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE consultation_results SET choice_id = '1610' WHERE choice_id = '768' AND question_id = '30220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';
UPDATE reponses_consultation SET choice_id = '1610' WHERE choice_id = '768' AND question_id = '30220' AND consultation_id = 'ei80m02y5koialuq4e30n4q6';

-- Consultation jf84upj9qutugwkce773m8dt / question 30177
UPDATE consultation_results SET choice_id = '1741' WHERE choice_id = '1013' AND question_id = '30177' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1741' WHERE choice_id = '1013' AND question_id = '30177' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1542' WHERE choice_id = '1014' AND question_id = '30177' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1542' WHERE choice_id = '1014' AND question_id = '30177' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '2090' WHERE choice_id = '1015' AND question_id = '30177' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '2090' WHERE choice_id = '1015' AND question_id = '30177' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE consultation_results SET choice_id = '1935' WHERE choice_id = '1016' AND question_id = '30177' AND consultation_id = 'jf84upj9qutugwkce773m8dt';
UPDATE reponses_consultation SET choice_id = '1935' WHERE choice_id = '1016' AND question_id = '30177' AND consultation_id = 'jf84upj9qutugwkce773m8dt';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 30125
UPDATE consultation_results SET choice_id = '1435' WHERE choice_id = '1077' AND question_id = '30125' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1435' WHERE choice_id = '1077' AND question_id = '30125' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1634' WHERE choice_id = '1078' AND question_id = '30125' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1634' WHERE choice_id = '1078' AND question_id = '30125' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1833' WHERE choice_id = '1079' AND question_id = '30125' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1833' WHERE choice_id = '1079' AND question_id = '30125' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 30126
UPDATE consultation_results SET choice_id = '1438' WHERE choice_id = '1097' AND question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1438' WHERE choice_id = '1097' AND question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1637' WHERE choice_id = '1098' AND question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1637' WHERE choice_id = '1098' AND question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1836' WHERE choice_id = '1099' AND question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1836' WHERE choice_id = '1099' AND question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '2008' WHERE choice_id = '1100' AND question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '2008' WHERE choice_id = '1100' AND question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '2149' WHERE choice_id = '1101' AND question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '2149' WHERE choice_id = '1101' AND question_id = '30126' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation hxec8jg448kdtr5uy4jhxt8y / question 30156
UPDATE consultation_results SET choice_id = '1508' WHERE choice_id = '1114' AND question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1508' WHERE choice_id = '1114' AND question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1707' WHERE choice_id = '1115' AND question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1707' WHERE choice_id = '1115' AND question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '1902' WHERE choice_id = '1116' AND question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '1902' WHERE choice_id = '1116' AND question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '2063' WHERE choice_id = '1117' AND question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '2063' WHERE choice_id = '1117' AND question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE consultation_results SET choice_id = '2195' WHERE choice_id = '1119' AND question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';
UPDATE reponses_consultation SET choice_id = '2195' WHERE choice_id = '1119' AND question_id = '30156' AND consultation_id = 'hxec8jg448kdtr5uy4jhxt8y';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 30212
UPDATE consultation_results SET choice_id = '1975' WHERE choice_id = '1265' AND question_id = '30212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1975' WHERE choice_id = '1265' AND question_id = '30212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1597' WHERE choice_id = '1266' AND question_id = '30212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1597' WHERE choice_id = '1266' AND question_id = '30212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1796' WHERE choice_id = '1267' AND question_id = '30212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1796' WHERE choice_id = '1267' AND question_id = '30212' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 30214
UPDATE consultation_results SET choice_id = '1600' WHERE choice_id = '1313' AND question_id = '30214' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1600' WHERE choice_id = '1313' AND question_id = '30214' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1799' WHERE choice_id = '1315' AND question_id = '30214' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1799' WHERE choice_id = '1315' AND question_id = '30214' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1978' WHERE choice_id = '1317' AND question_id = '30214' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1978' WHERE choice_id = '1317' AND question_id = '30214' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 30225
UPDATE consultation_results SET choice_id = '1623' WHERE choice_id = '1328' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1623' WHERE choice_id = '1328' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1822' WHERE choice_id = '1329' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1822' WHERE choice_id = '1329' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1997' WHERE choice_id = '1330' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1997' WHERE choice_id = '1330' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '2247' WHERE choice_id = '1331' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '2247' WHERE choice_id = '1331' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '2328' WHERE choice_id = '1332' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '2328' WHERE choice_id = '1332' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '2138' WHERE choice_id = '1333' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '2138' WHERE choice_id = '1333' AND question_id = '30225' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 30213
UPDATE consultation_results SET choice_id = '1599' WHERE choice_id = '1309' AND question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1599' WHERE choice_id = '1309' AND question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1798' WHERE choice_id = '1310' AND question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1798' WHERE choice_id = '1310' AND question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1977' WHERE choice_id = '1311' AND question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1977' WHERE choice_id = '1311' AND question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '2235' WHERE choice_id = '1312' AND question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '2235' WHERE choice_id = '1312' AND question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '2124' WHERE choice_id = '1314' AND question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '2124' WHERE choice_id = '1314' AND question_id = '30213' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 30227
UPDATE consultation_results SET choice_id = '1826' WHERE choice_id = '1348' AND question_id = '30227' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1826' WHERE choice_id = '1348' AND question_id = '30227' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1627' WHERE choice_id = '1349' AND question_id = '30227' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1627' WHERE choice_id = '1349' AND question_id = '30227' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation eykw95ahuc3fb5husp5wnjp7 / question 30226
UPDATE consultation_results SET choice_id = '1626' WHERE choice_id = '1351' AND question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1626' WHERE choice_id = '1351' AND question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '2000' WHERE choice_id = '1352' AND question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '2000' WHERE choice_id = '1352' AND question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '2250' WHERE choice_id = '1353' AND question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '2250' WHERE choice_id = '1353' AND question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '1825' WHERE choice_id = '1354' AND question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '1825' WHERE choice_id = '1354' AND question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE consultation_results SET choice_id = '2141' WHERE choice_id = '1355' AND question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';
UPDATE reponses_consultation SET choice_id = '2141' WHERE choice_id = '1355' AND question_id = '30226' AND consultation_id = 'eykw95ahuc3fb5husp5wnjp7';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 30155
UPDATE consultation_results SET choice_id = '1505' WHERE choice_id = '1420' AND question_id = '30155' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1505' WHERE choice_id = '1420' AND question_id = '30155' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1704' WHERE choice_id = '1422' AND question_id = '30155' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1704' WHERE choice_id = '1422' AND question_id = '30155' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1899' WHERE choice_id = '1423' AND question_id = '30155' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1899' WHERE choice_id = '1423' AND question_id = '30155' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- Consultation js6vc1xuysnj4kl5n9tdcfrq / question 30154
UPDATE consultation_results SET choice_id = '1504' WHERE choice_id = '1416' AND question_id = '30154' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1504' WHERE choice_id = '1416' AND question_id = '30154' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1703' WHERE choice_id = '1417' AND question_id = '30154' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1703' WHERE choice_id = '1417' AND question_id = '30154' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '1898' WHERE choice_id = '1418' AND question_id = '30154' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '1898' WHERE choice_id = '1418' AND question_id = '30154' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE consultation_results SET choice_id = '2060' WHERE choice_id = '1421' AND question_id = '30154' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';
UPDATE reponses_consultation SET choice_id = '2060' WHERE choice_id = '1421' AND question_id = '30154' AND consultation_id = 'js6vc1xuysnj4kl5n9tdcfrq';

-- ============================================================
-- APRÈS EXÉCUTION : invalider le cache Redis
-- redis-cli KEYS "consultation_results_*" | xargs redis-cli DEL
-- ============================================================
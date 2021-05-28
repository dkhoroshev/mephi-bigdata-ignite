#!/usr/bin/env python
# -*- coding: utf-8 -*-
# (C) Dmitriy Horoshev

import time, datetime, random
from faker import Faker
from itertools import groupby

# import sys
# reload(sys)
# sys.setdefaultencoding('utf8')

faker = Faker()
MAIN_ENTITY_COUNT = 1000    #Кол-во записей
USER_COUNT = 10

# Создание списка университетов
collage = list()
for _ in range(USER_COUNT):
    collage.append(random.randint(1,25))
# Сортируем и удаляем повторения
collage.sort()
collage = [el for el, _ in groupby(collage)]

# Генерация ID студентов/преподавателей и привязка их к университету
comblist = dict.fromkeys(collage, list())
for c in collage:
    uids = list()
    for _ in range(USER_COUNT*2):
        uids.append(random.randint(1,250))
    # Сортируем и удаляем повторения
    uids.sort()
    uids = [el for el, _ in groupby(uids)]
    # print("collage: %s => uids: %s" % (c,str(uids)))
    comblist[c] = uids

# Генерация журнала посещений
# Выходной формат: универ,uid,время,0/1(выход/вход)
time = []
for c,uids in comblist.items():
    for uid in uids:
        for _ in range(random.randrange(MAIN_ENTITY_COUNT)):
            startdate=faker.date_time_ad(start_datetime='-5y')
            enddate=startdate+datetime.timedelta(hours=random.randrange(5))
            time.append(str(c) + ',' + str(uid) + ',' + str(startdate) + ',1\n')
            time.append(str(c) + ',' + str(uid) + ',' + str(enddate) + ',0\n')

ftime = open('journal.txt', 'w')
ftime.writelines(time)
ftime.close()

# Генерация списка публикаций
# Выходной формат: универ,uid,дата,название
publications = []
for c,uids in comblist.items():
    for uid in uids:
        for _ in range(random.randrange(MAIN_ENTITY_COUNT/40)):
            dateP=faker.date_between(start_date='-5y', end_date='today')
            publications.append(str(c) + ',' + str(uid) + ',' + str(dateP) + ',' + faker.text(max_nb_chars=50) + '\n')

fpublications = open('publications.txt', 'w')
fpublications.writelines(publications)
fpublications.close()
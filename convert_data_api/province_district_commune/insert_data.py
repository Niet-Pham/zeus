import json
import mysql.connector

conn = mysql.connector.connect(
    user='root',
    password='1111',
    host='127.0.0.1',
    database='k2_tech_store',
    auth_plugin='mysql_native_password'
)
cur = conn.cursor()

try:
    cur.execute('SET FOREIGN_KEY_CHECKS = 0;')
    cur.execute('truncate table commune;')
    cur.execute('SET FOREIGN_KEY_CHECKS = 1;')
    cur.execute('SET FOREIGN_KEY_CHECKS = 0;')
    cur.execute('truncate table district;')
    cur.execute('SET FOREIGN_KEY_CHECKS = 1;')
    cur.execute('SET FOREIGN_KEY_CHECKS = 0;')
    cur.execute('truncate table province;')
    cur.execute('SET FOREIGN_KEY_CHECKS = 1;')

    with open('data.json') as data:
        for d in data:
            json = json.loads(d)
            for p in json:
                insert_province = "insert into province(name) values (\"%s\")" % p['Name']
                print(insert_province + '\n')
                cur.execute(insert_province)

                get_last_id_province = "select province_id from province order by province_id desc limit 1"
                cur.execute(get_last_id_province)
                last_province_id = cur.fetchone()

                for d in p['Districts']:
                    insert_district = "insert into district(province_id, name) values (%d, \"%s\")" % (int(last_province_id[0]), d['Name'])
                    print(insert_district + '\n')
                    cur.execute(insert_district)

                    get_last_id_district = "select district_id from district order by district_id desc limit 1"
                    cur.execute(get_last_id_district)
                    last_district_id = cur.fetchone()

                    for c in d['Wards']:
                        if len(c) < 3:
                            break
                        insert_commune = "insert into commune(province_id, district_id, name) values (%d, %d, \"%s\")" % (int(last_province_id[0]), int(last_district_id[0]), c['Name'])
                        print(insert_commune + '\n')
                        cur.execute(insert_commune)
    conn.commit()
    conn.close()
except Exception as e:
    print(e)

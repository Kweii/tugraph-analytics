/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

CREATE TABLE IF NOT EXISTS join_to_match_007_result (
	studentId1 bigint,
	studentName varchar,
	studentAge bigint,
  teacherId1 bigint,
  teacherName varchar,
  studentTeacherId varchar,
  studentTeacher varchar
) WITH (
	type='file',
	geaflow.dsl.file.path='${target}'
);

USE GRAPH g_student;

INSERT INTO join_to_match_007_result
SELECT studentId1, studentName, studentAge, teacherId1, teacherName, studentTeacherId,
       concat(studentName,teacherName)
FROM
(
    SELECT studentId1, studentName, studentAge, m.srcId as studentId2, m.targetId as teacherId1,
           concat(studentName, cast(m.targetId as varchar)) as studentTeacherId
    FROM
        (
        SELECT studentId1,
        IF(studentAge > 21,concat('>21',studentName),concat('<=21',studentName)) as
        studentName,
        studentAge
        FROM
        (
            SELECT s.id as studentId1, concat('Student-', s.name) as studentName, s.age as studentAge
            FROM student s
            WHERE s.age > 19
        )
        ), hasMonitor m
    WHERE studentId1 = m.srcId
)
, (
    SELECT t.id as teacherId2, concat('Monitor-', t.name) as teacherName
    FROM teacher t
    WHERE t.id > 9001
)
WHERE
teacherId1 = teacherId2
ORDER BY studentId1, teacherId1
;
import { Schedule } from '@/@types';
import { CategoryType } from '@/@types/category';

const scheduleDB: Schedule[] = [
  {
    id: 1,
    title: '우테코 데모데이',
    startDateTime: '2022-07-07T14:00',
    endDateTime: '2022-07-07T18:00',
    memo: '달록팀 스프린트 1차 발표',
  },
  {
    id: 2,
    title: '테코톡',
    startDateTime: '2022-07-09T14:00',
    endDateTime: '2022-07-10T20:00',
    memo: '레벨3 첫 테코톡',
  },
];

const categoryDB: {
  data: CategoryType[];
} = {
  data: [
    {
      id: 1,
      name: 'BE 공식일정',
      createdAt: '2022-07-04T13:00:00',
    },
    {
      id: 2,
      name: '알록달록 팀 회의',
      createdAt: '2022-07-08T15:00:00',
    },
    {
      id: 3,
      name: '카테고리 3',
      createdAt: '2022-07-05T13:00:00',
    },
    {
      id: 4,
      name: '카테고리 4',
      createdAt: '2022-07-03T15:00:00',
    },
    {
      id: 5,
      name: '카테고리 5',
      createdAt: '2022-07-02T13:00:00',
    },
    {
      id: 6,
      name: '카테고리 6',
      createdAt: '2022-07-01T15:00:00',
    },
    {
      id: 7,
      name: '카테고리 7',
      createdAt: '2022-07-11T13:00:00',
    },
    {
      id: 8,
      name: '카테고리 8',
      createdAt: '2022-07-12T15:00:00',
    },
    {
      id: 9,
      name: '카테고리 9',
      createdAt: '2022-07-13T13:00:00',
    },
    {
      id: 10,
      name: '카테고리 10',
      createdAt: '2022-07-14T15:00:00',
    },
    {
      id: 11,
      name: '카테고리 11',
      createdAt: '2022-07-15T13:00:00',
    },
    {
      id: 12,
      name: '카테고리 12',
      createdAt: '2022-07-16T15:00:00',
    },
    {
      id: 13,
      name: '카테고리 13',
      createdAt: '2022-07-18T13:00:00',
    },
    {
      id: 14,
      name: '카테고리 14',
      createdAt: '2022-07-20T15:00:00',
    },
    {
      id: 15,
      name: '카테고리 15',
      createdAt: '2022-07-12T13:00:00',
    },
    {
      id: 16,
      name: '카테고리 16',
      createdAt: '2022-07-13T15:00:00',
    },
    {
      id: 17,
      name: '카테고리 17',
      createdAt: '2022-07-25T13:00:00',
    },
    {
      id: 18,
      name: '카테고리 18',
      createdAt: '2022-07-30T15:00:00',
    },
  ],
};

export { scheduleDB, categoryDB };

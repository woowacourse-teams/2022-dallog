const profileDB = {
  id: 1,
  email: 'example@email.com',
  displayName: '매트',
  profileImageUrl: 'https://img.insight.co.kr/static/2020/05/04/700/51wlu5y2281iku1o0hms.jpg',
  socialType: 'GOOGLE',
};

const categoryDB = {
  categories: [
    {
      id: 1,
      name: 'BE 공식일정',
      createdAt: '2022-07-04T13:00:00',
      creator: profileDB,
    },
    {
      id: 2,
      name: '알록달록 팀 회의',
      createdAt: '2022-07-08T15:00:00',
      creator: profileDB,
    },
    {
      id: 3,
      name: '카테고리 3',
      createdAt: '2022-07-05T13:00:00',
      creator: profileDB,
    },
    {
      id: 4,
      name: '카테고리 4',
      createdAt: '2022-07-03T15:00:00',
      creator: profileDB,
    },
    {
      id: 5,
      name: '카테고리 5',
      createdAt: '2022-07-02T13:00:00',
      creator: profileDB,
    },
    {
      id: 6,
      name: '카테고리 6',
      createdAt: '2022-07-01T15:00:00',
      creator: profileDB,
    },
    {
      id: 7,
      name: '카테고리 7',
      createdAt: '2022-07-11T13:00:00',
      creator: profileDB,
    },
    {
      id: 8,
      name: '카테고리 8',
      createdAt: '2022-07-12T15:00:00',
      creator: profileDB,
    },
    {
      id: 9,
      name: '카테고리 9',
      createdAt: '2022-07-13T13:00:00',
      creator: profileDB,
    },
    {
      id: 10,
      name: '카테고리 10',
      createdAt: '2022-07-14T15:00:00',
      creator: profileDB,
    },
    {
      id: 11,
      name: '카테고리 11',
      createdAt: '2022-07-15T13:00:00',
      creator: profileDB,
    },
    {
      id: 12,
      name: '카테고리 12',
      createdAt: '2022-07-16T15:00:00',
      creator: profileDB,
    },
    {
      id: 13,
      name: '카테고리 13',
      createdAt: '2022-07-18T13:00:00',
      creator: profileDB,
    },
    {
      id: 14,
      name: '카테고리 14',
      createdAt: '2022-07-20T15:00:00',
      creator: profileDB,
    },
    {
      id: 15,
      name: '카테고리 15',
      createdAt: '2022-07-12T13:00:00',
      creator: profileDB,
    },
    {
      id: 16,
      name: '카테고리 16',
      createdAt: '2022-07-13T15:00:00',
      creator: profileDB,
    },
    {
      id: 17,
      name: '카테고리 17',
      createdAt: '2022-07-25T13:00:00',
      creator: profileDB,
    },
    {
      id: 18,
      name: '카테고리 18',
      createdAt: '2022-07-30T15:00:00',
      creator: profileDB,
    },
  ],
};

const myCategoryDB = {
  categories: [
    {
      id: 1,
      name: '나의 카테고리 1',
      createdAt: '2022-07-04T13:00:00',
      creator: profileDB,
    },
    {
      id: 2,
      name: '나의 카테고리 2',
      createdAt: '2022-07-08T15:00:00',
      creator: profileDB,
    },
    {
      id: 3,
      name: '나의 카테고리 3',
      createdAt: '2022-07-05T13:00:00',
      creator: profileDB,
    },
    {
      id: 4,
      name: '나의 카테고리 4',
      createdAt: '2022-07-03T15:00:00',
      creator: profileDB,
    },
    {
      id: 5,
      name: '나의 카테고리 5',
      createdAt: '2022-07-02T13:00:00',
      creator: profileDB,
    },
    {
      id: 6,
      name: '나의 카테고리 6',
      createdAt: '2022-07-01T15:00:00',
      creator: profileDB,
    },
    {
      id: 7,
      name: '나의 카테고리 7',
      createdAt: '2022-07-11T13:00:00',
      creator: profileDB,
    },
    {
      id: 8,
      name: '나의 카테고리 8',
      createdAt: '2022-07-12T15:00:00',
      creator: profileDB,
    },
    {
      id: 9,
      name: '나의 카테고리 9',
      createdAt: '2022-07-13T13:00:00',
      creator: profileDB,
    },
    {
      id: 10,
      name: '나의 카테고리 10',
      createdAt: '2022-07-14T15:00:00',
      creator: profileDB,
    },
    {
      id: 11,
      name: '나의 카테고리 11',
      createdAt: '2022-07-15T13:00:00',
      creator: profileDB,
    },
    {
      id: 12,
      name: '나의 카테고리 12',
      createdAt: '2022-07-16T15:00:00',
      creator: profileDB,
    },
    {
      id: 13,
      name: '나의 카테고리 13',
      createdAt: '2022-07-18T13:00:00',
      creator: profileDB,
    },
    {
      id: 14,
      name: '나의 카테고리 14',
      createdAt: '2022-07-20T15:00:00',
      creator: profileDB,
    },
    {
      id: 15,
      name: '나의 카테고리 15',
      createdAt: '2022-07-12T13:00:00',
      creator: profileDB,
    },
    {
      id: 16,
      name: '나의 카테고리 16',
      createdAt: '2022-07-13T15:00:00',
      creator: profileDB,
    },
    {
      id: 17,
      name: '나의 카테고리 17',
      createdAt: '2022-07-25T13:00:00',
      creator: profileDB,
    },
    {
      id: 18,
      name: '나의 카테고리 18',
      createdAt: '2022-07-30T15:00:00',
      creator: profileDB,
    },
    {
      id: 19,
      name: '나의 카테고리 19',
      createdAt: '2022-07-30T15:00:00',
      creator: profileDB,
    },
    {
      id: 20,
      name: '나의 카테고리 20',
      createdAt: '2022-07-30T15:00:00',
      creator: profileDB,
    },
  ],
};

const scheduleDB = [
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

const subscriptionDB = {
  subscriptions: [
    {
      id: 1,
      category: {
        id: 1,
        name: 'BE 공식일정',
        creator: {
          id: 1,
          email: 'example@email.com',
          displayName: 'example',
          profileImageUrl: '/image.png',
        },
        createdAt: '2022-07-19T16:42',
      },
      color: '#ffffff',
    },
    {
      id: 2,
      category: {
        id: 2,
        name: '알록달록 팀 회의',
        creator: {
          id: 1,
          email: 'example@email.com',
          displayName: 'example',
          profileImageUrl: '/image.png',
        },
        createdAt: '2022-07-19T16:42',
      },
      color: '#123423',
    },
    {
      id: 3,
      category: {
        id: 5,
        name: '카테고리 5',
        creator: {
          id: 1,
          email: 'example@email.com',
          displayName: 'example',
          profileImageUrl: '/image.png',
        },
        createdAt: '2022-07-19T16:42',
      },
      color: '#876453',
    },
  ],
};

export { categoryDB, myCategoryDB, profileDB, scheduleDB, subscriptionDB };

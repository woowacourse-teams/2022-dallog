import { ScheduleType } from '@/@types/schedule';

const matProfileDB = {
  id: 1,
  email: 'example@email.com',
  displayName: '매트',
  profileImageUrl: 'https://img.insight.co.kr/static/2020/05/04/700/51wlu5y2281iku1o0hms.jpg',
  socialType: 'GOOGLE',
};

const tigerProfileDB = {
  id: 2,
  email: 'tiger@dallog.com',
  displayName: '티거',
  profileImageUrl:
    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRBo4PPBaz3xqgKPEs8W2djKBXYb2D8PFDkZg&usqp=CAU',
  socialType: 'GOOGLE',
};

const categoryDB = {
  categories: [
    {
      id: 1,
      name: 'BE 공식 일정',
      createdAt: '2022-07-04T13:00:00',
      creator: matProfileDB,
    },
    {
      id: 2,
      name: '알록달록',
      createdAt: '2022-07-08T15:00:00',
      creator: matProfileDB,
    },
    {
      id: 3,
      name: '레벨 1 공원조',
      createdAt: '2022-07-05T13:00:00',
      creator: tigerProfileDB,
    },
    {
      id: 4,
      name: '자바 스터디',
      createdAt: '2022-07-03T15:00:00',
      creator: matProfileDB,
    },
    {
      id: 5,
      name: '코틀린 스터디',
      createdAt: '2022-07-02T13:00:00',
      creator: matProfileDB,
    },
    {
      id: 6,
      name: '지원 플랫폼 근로',
      createdAt: '2022-07-01T15:00:00',
      creator: matProfileDB,
    },
    {
      id: 7,
      name: '레벨 2 준조',
      createdAt: '2022-07-08T15:00:00',
      creator: tigerProfileDB,
    },
    {
      id: 8,
      name: '레벨 1 네오조',
      createdAt: '2022-07-12T15:00:00',
      creator: matProfileDB,
    },
    {
      id: 9,
      name: '매트의 아고라',
      createdAt: '2022-07-13T13:00:00',
      creator: matProfileDB,
    },
    {
      id: 10,
      name: '자바스크립트 스터디',
      createdAt: '2022-07-05T13:00:00',
      creator: tigerProfileDB,
    },
    {
      id: 11,
      name: '리액트 스터디',
      createdAt: '2022-07-03T15:00:00',
      creator: tigerProfileDB,
    },
    {
      id: 12,
      name: 'FE 공식 일정',
      createdAt: '2022-07-16T15:00:00',
      creator: tigerProfileDB,
    },
    {
      id: 13,
      name: '레벨 2 브리조',
      createdAt: '2022-07-18T13:00:00',
      creator: matProfileDB,
    },
    {
      id: 14,
      name: '세미나',
      createdAt: '2022-07-20T15:00:00',
      creator: matProfileDB,
    },
    {
      id: 15,
      name: '네오 면담',
      createdAt: '2022-07-01T15:00:00',
      creator: tigerProfileDB,
    },
    {
      id: 16,
      name: '공원 면담',
      createdAt: '2022-07-11T13:00:00',
      creator: tigerProfileDB,
    },
    {
      id: 17,
      name: '포코 면담',
      createdAt: '2022-07-12T15:00:00',
      creator: tigerProfileDB,
    },
    {
      id: 18,
      name: '포비의 수다 타임',
      createdAt: '2022-07-13T13:00:00',
      creator: matProfileDB,
    },
    {
      id: 19,
      name: '튼튼이 클럽',
      createdAt: '2022-07-13T13:00:00',
      creator: tigerProfileDB,
    },
    {
      id: 20,
      name: '한강팟',
      createdAt: '2022-07-14T15:00:00',
      creator: tigerProfileDB,
    },
  ],
};

const myCategoryDB = {
  categories: [
    {
      id: 3,
      name: '레벨 1 공원조',
      createdAt: '2022-07-05T13:00:00',
      creator: tigerProfileDB,
    },
    {
      id: 7,
      name: '레벨 2 준조',
      createdAt: '2022-07-08T15:00:00',
      creator: tigerProfileDB,
    },
    {
      id: 10,
      name: '자바스크립트 스터디',
      createdAt: '2022-07-05T13:00:00',
      creator: tigerProfileDB,
    },
    {
      id: 11,
      name: '리액트 스터디',
      createdAt: '2022-07-03T15:00:00',
      creator: tigerProfileDB,
    },
    {
      id: 12,
      name: 'FE 공식 일정',
      createdAt: '2022-07-16T15:00:00',
      creator: tigerProfileDB,
    },
    {
      id: 15,
      name: '네오 면담',
      createdAt: '2022-07-01T15:00:00',
      creator: tigerProfileDB,
    },
    {
      id: 16,
      name: '공원 면담',
      createdAt: '2022-07-11T13:00:00',
      creator: tigerProfileDB,
    },
    {
      id: 17,
      name: '포코 면담',
      createdAt: '2022-07-12T15:00:00',
      creator: tigerProfileDB,
    },
    {
      id: 19,
      name: '튼튼이 클럽',
      createdAt: '2022-07-13T13:00:00',
      creator: tigerProfileDB,
    },
    {
      id: 20,
      name: '한강팟',
      createdAt: '2022-07-14T15:00:00',
      creator: tigerProfileDB,
    },
  ],
};

const scheduleDB = [
  {
    id: 1,
    title: '공원조 회식',
    startDateTime: '2022-07-07T18:00',
    endDateTime: '2022-07-07T22:00',
    memo: '잠실역',
    categoryId: 3,
  },
  {
    id: 2,
    title: 'Typescript 강의',
    startDateTime: '2022-07-09T10:00',
    endDateTime: '2022-07-10T12:30',
    memo: '포코',
    categoryId: 12,
  },
  {
    id: 3,
    title: '어깨',
    startDateTime: '2022-07-17T00:00',
    endDateTime: '2022-07-17T23:59',
    memo: '',
    categoryId: 19,
  },
  {
    id: 4,
    title: '준조 MT',
    startDateTime: '2022-07-17T17:00',
    endDateTime: '2022-07-21T07:00',
    memo: '',
    categoryId: 7,
  },
  {
    id: 5,
    title: '제주 여행',
    startDateTime: '2022-07-04T13:00',
    endDateTime: '2022-07-06T07:00',
    memo: '✈️',
    categoryId: 20,
  },
  {
    id: 6,
    title: '공원조 MT',
    startDateTime: '2022-07-23T13:00',
    endDateTime: '2022-07-23T15:00',
    memo: '리액트 MT',
    categoryId: 13,
  },
];

const getScheduleDB: { [key: string]: ScheduleType[] } = {
  longTerms: [
    {
      id: 1,
      title: '공원조 회식',
      startDateTime: '2022-08-04T13:00',
      endDateTime: '2022-08-06T07:00',
      memo: '잠실역',
      categoryId: 3,
      colorCode: '#F37970',
    },
    {
      id: 2,
      title: '준조 MT',
      startDateTime: '2022-08-17T13:00',
      endDateTime: '2022-08-21T07:00',
      memo: '',
      categoryId: 7,
      colorCode: '#FEDA15',
    },
    {
      id: 3,
      title: '제주 여행',
      startDateTime: '2022-08-05T13:00',
      endDateTime: '2022-08-22T07:00',
      memo: '✈️',
      categoryId: 20,
      colorCode: '#0C2D48',
    },
  ],

  allDays: [
    {
      id: 4,
      title: '어깨',
      startDateTime: '2022-08-17T00:00',
      endDateTime: '2022-08-17T23:59',
      memo: '',
      categoryId: 19,
      colorCode: '#695E93',
    },
  ],

  fewHours: [
    {
      id: 5,
      title: '모의 면접',
      startDateTime: '2022-08-23T13:00',
      endDateTime: '2022-08-23T15:00',
      memo: '실행 컨텍스트',
      categoryId: 10,
      colorCode: '#81B622',
    },
  ],
};

const subscriptionDB = {
  subscriptions: [
    {
      id: 1,
      category: {
        id: 3,
        name: '레벨 1 공원조',
        createdAt: '2022-07-05T13:00:00',
        creator: tigerProfileDB,
      },
      colorCode: '#F37970',
      checked: true,
    },
    {
      id: 2,
      category: {
        id: 7,
        name: '레벨 2 준조',
        createdAt: '2022-07-08T15:00:00',
        creator: tigerProfileDB,
      },
      colorCode: '#FEDA15',
      checked: true,
    },
    {
      id: 3,
      category: {
        id: 10,
        name: '자바스크립트 스터디',
        createdAt: '2022-07-05T13:00:00',
        creator: tigerProfileDB,
      },
      colorCode: '#81B622',
      checked: true,
    },
    {
      id: 4,
      category: {
        id: 11,
        name: '리액트 스터디',
        createdAt: '2022-07-03T15:00:00',
        creator: tigerProfileDB,
      },
      colorCode: '#145DA0',
      checked: true,
    },
    {
      id: 5,
      category: {
        id: 12,
        name: 'FE 공식 일정',
        createdAt: '2022-07-16T15:00:00',
        creator: tigerProfileDB,
      },
      colorCode: '#464033',
      checked: true,
    },
    {
      id: 6,
      category: {
        id: 15,
        name: '네오 면담',
        createdAt: '2022-07-01T15:00:00',
        creator: tigerProfileDB,
      },
      colorCode: '#7E7C73',
      checked: false,
    },
    {
      id: 7,
      category: {
        id: 16,
        name: '공원 면담',
        createdAt: '2022-07-11T13:00:00',
        creator: tigerProfileDB,
      },
      colorCode: '#7E7C73',
      checked: false,
    },
    {
      id: 8,
      category: {
        id: 17,
        name: '포코 면담',
        createdAt: '2022-07-12T15:00:00',
        creator: tigerProfileDB,
      },
      colorCode: '#7E7C73',
      checked: false,
    },
    {
      id: 9,
      category: {
        id: 19,
        name: '튼튼이 클럽',
        createdAt: '2022-07-13T13:00:00',
        creator: tigerProfileDB,
      },
      colorCode: '#695E93',
      checked: true,
    },
    {
      id: 10,
      category: {
        id: 20,
        name: '한강팟',
        createdAt: '2022-07-14T15:00:00',
        creator: tigerProfileDB,
      },
      colorCode: '#0C2D48',
      checked: true,
    },
  ],
};

export {
  categoryDB,
  getScheduleDB,
  matProfileDB,
  myCategoryDB,
  scheduleDB,
  subscriptionDB,
  tigerProfileDB,
};

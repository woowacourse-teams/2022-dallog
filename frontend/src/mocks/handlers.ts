import { rest } from 'msw';

interface PostScheduleReqBody {
  title: string;
  startDateTime: string;
  endDateTime: string;
  memo: string;
}

interface ScheduleResponse extends PostScheduleReqBody {
  id: number;
}

const scheduleDB: ScheduleResponse[] = [
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

const handlers = [
  rest.post<PostScheduleReqBody>('/api/schedules', (req, res, ctx) => {
    scheduleDB.push({ id: scheduleDB.length + 1, ...req.body });

    return res(ctx.status(201));
  }),

  rest.get('/api/schedules', (req, res, ctx) => {
    return res(ctx.status(200), ctx.json({ data: scheduleDB }));
  }),
];

export { handlers };

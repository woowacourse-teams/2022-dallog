import { rest } from 'msw';

interface PostScheduleReqBody {
  title: string;
  startDateTime: string;
  endDateTime: string;
  memo: string;
}

const scheduleDB = [];

const handlers = [
  rest.post<PostScheduleReqBody>('/api/schedules', (req, res, ctx) => {
    scheduleDB.push(req.body);

    return res(ctx.status(201));
  }),
];

export { handlers };

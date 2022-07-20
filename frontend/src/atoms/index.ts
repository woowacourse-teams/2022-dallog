import { atom } from 'recoil';

import { ATOM_KEY } from '@/constants';

import { getAccessToken } from '@/utils';

const userState = atom({
  key: ATOM_KEY.USER,
  default: {
    accessToken: getAccessToken(),
  },
});

export { userState };

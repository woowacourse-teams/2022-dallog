import { css, Theme } from '@emotion/react';

import { ModalPosType } from '@/@types';

const scheduleModalStyle = ({ colors }: Theme, scheduleModalPos: ModalPosType) => css`
  position: absolute;
  top: ${scheduleModalPos.top ? `${scheduleModalPos.top + 20}px` : 'none'};
  right: ${scheduleModalPos.right ? `${scheduleModalPos.right + 20}px` : 'none'};
  bottom: ${scheduleModalPos.bottom ? `${scheduleModalPos.bottom + 20}px` : 'none'};
  left: ${scheduleModalPos.left ? `${scheduleModalPos.left + 20}px` : 'none'};

  padding: 5rem 5rem 10rem 10rem;
  border-radius: 8px;
  box-shadow: 0 0 30px ${colors.GRAY_500};

  background: ${colors.WHITE};
`;

const headerStyle = ({ flex }: Theme) => css`
  ${flex.row};

  justify-content: flex-end;
`;

const buttonStyle = ({ colors }: Theme) => css`
  position: relative;

  width: 11rem;
  height: 11rem;

  font-size: 5rem;
  color: ${colors.GRAY_700};

  &:hover {
    border-radius: 50%;

    background: ${colors.GRAY_100};

    filter: none;
  }

  &:hover span {
    visibility: visible;
  }
`;

const buttonTitleStyle = ({ colors }: Theme) => css`
  visibility: hidden;
  position: absolute;
  top: 120%;
  left: 50%;
  transform: translateX(-50%);

  padding: 2rem 3rem;

  background: ${colors.GRAY_700}ee;

  font-size: 3rem;
  font-weight: normal;
  color: ${colors.WHITE};
  white-space: nowrap;
`;

const contentStyle = ({ flex }: Theme) => css`
  ${flex.column}

  gap: 10rem;
`;

const contentBlockStyle = ({ colors }: Theme) => css`
  display: flex;
  gap: 3rem;

  width: 90rem;

  font-size: 4rem;
  color: ${colors.GRAY_700};
`;

const scheduleIconStyle = css`
  margin-top: 1rem;
`;

const scheduleInfoStyle = ({ flex }: Theme) => css`
  ${flex.column}

  align-items: flex-start;
  gap: 3rem;
`;

const scheduleTitleStyle = css`
  font-size: 6rem;
`;

const colorStyle = (colorCode: string | undefined) => css`
  width: 4rem;
  height: 4rem;
  border-radius: 25%;

  background: ${colorCode};
`;

export {
  buttonStyle,
  buttonTitleStyle,
  colorStyle,
  contentBlockStyle,
  contentStyle,
  headerStyle,
  scheduleIconStyle,
  scheduleInfoStyle,
  scheduleModalStyle,
  scheduleTitleStyle,
};

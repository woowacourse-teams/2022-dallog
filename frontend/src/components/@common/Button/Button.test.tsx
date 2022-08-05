/**
 * @jest-environment jsdom
 */
import { composeStories } from '@storybook/testing-react';
import { render, screen } from '@testing-library/react';

import * as stories from './Button.stories';

const { Primary } = composeStories(stories);

test('기본 버튼이 인자들과 함께 출력된다.', () => {
  render(<Primary />);

  const buttonElement = screen.getByText(/기본 버튼입니다./i);

  expect(buttonElement).not.toBeNull();
});

test('기본 버튼의 props를 overwrite할 수 있다.', () => {
  render(<Primary>달록 버튼</Primary>);

  const buttonElement = screen.getByText(/달록 버튼/i);

  expect(buttonElement).not.toBeNull();
});

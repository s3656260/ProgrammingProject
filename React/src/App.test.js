import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import fetchMock from 'fetch-mock';
import api from '../src/pages/api';
import home from '../src/pages/Home';
// global.fetch = require('jest-fetch-mock')



// it('renders without crashing', () => {
//   const div = document.createElement('div');
//   ReactDOM.render(<App />, div);
//   ReactDOM.unmountComponentAtNode(div);
// });


it('Api test case', async function () {
  global.fetch - jest.fn().mockImplementation(() => {
    var p = new Promise((resolve, reject) => {
      resolve({
        json: function () {
          return { Id: 1 }

        }

      })

    })
    return p;

  })

  const response = await api.all();
  //console.warn(response)
  expect(response.Id).toBe(1)

});

describe('List row component', () => {
  it('Should render without errors', () => {
    const component = shallow(<Div />);
    const wrapper = component.find('.TableData');
    expect(wrapper.length).toBe(1);

  })
});


test('Fake test', () => {
  expect(true).toBeTruthy();

});

import React, { Component } from 'react';
import List from './List';
import * as api from '../../api/api';

import './Lists.css';
import CreateList from './CreateList';

export default class Lists extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentList: {},
            lists: [],
        };
        this.createTile = this.createTile.bind(this);
        this.updateList = this.updateList.bind(this);
        this.updateCurrentList = this.updateCurrentList.bind(this);
        this.deleteList = this.deleteList.bind(this);
        this.getLists = this.getLists.bind(this);
    }

    async componentDidMount() {
        this.getLists();
    }

    async getLists() {
        const lists = await api.getLists();
        const currentList = await api.getCurrentList();
        this.setState({
            lists,
            currentList,
        });
    }

    async updateCurrentList(list) {
        const response = await api.updateCurrentList(list);
        this.setState({ currentList: response.currentList });
    }

    async updateList(list) {
        const response = await api.updateList(list);
        this.setState({ lists: response.lists });
    }

    async deleteList(name) {
        const response = await api.deleteList(name);
        this.setState({ lists: response.lists });
    }

    createTile(list) {
        const { currentList } = this.state;
        return (
            <List
                key={list.name}
                name={list.name}
                content={list.content}
                currentListName={currentList.name}
                deleteList={this.deleteList}
                updateList={this.updateList}
                updateCurrentList={this.updateCurrentList}
            />
        );
    }

    render() {
        const { currentList, lists } = this.state;
        return (
            <div id="container">
                <h1 id="h1">Lists</h1>

                <div id="currentAndCreate">
                    <div id="current">
                        <h2 id="h2">Current List</h2>
                        <p id="note">This is the current list being used to display bugs in the table.</p>
                        {this.createTile(currentList)}
                    </div>

                    <div id="create">
                        <h2 id="h2">Create List</h2>
                        <p id="note">Create a new list here.</p>
                        <CreateList getLists={this.getLists} />
                    </div>
                </div>

                <br />

                <h2 id="h2">Available Lists</h2>
                <div id="lists">
                    {lists.map((list) => {
                        if (list.name !== currentList.name) {
                            return this.createTile(list);
                        }
                        return null;
                    })}
                </div>
            </div>
        );
    }
}

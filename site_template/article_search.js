const articleList = [];

$.get(
	'article_list.json',
	{},
	function(data) {
		data.forEach((row, index) => {
			console.log(row);

			const article = {
				id: row.id,
				regDate: row.regDate,
				writer: row.extra_writer,
				title: row.title,
				body: row.body,
				rcmCount: row.rcmCount
			};

			articleList.push(article);
		});
	},
	'json'
);

const articleListBoxVue = new Vue({
	el: "#article-list-wrap",
	data: {
		articleList: articleList,
		searchKeyword: ''
	},
	methods: {
		searchKeywordInputed: _.debounce(function(e) {
			this.searchKeyword = e.target.value;
		}, 500)
	},
	computed: {
		filterKey: function() {
			return this.searchKeyword.toLowerCase();
		},
		filtered: function() {
			if (this.filterKey.length == 0) {
				return this.articleList;
			}

			return this.articleList.filter((row) => {
				const keys = ['title', 'writer', 'body', 'regDate'];

				const match = keys.some((key) => {
					if (row[key].toLowerCase().indexOf(this.filterKey) > -1) {
						return true;
					}
				});

				return match;
			});
		}
	}
}); 
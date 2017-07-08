import json


template = {
    'messages': {
        'enter_dungeon_messages': [
            'ENTRY MESSAGE 1',
            'ENTRY MESSAGE 2',
            'ENTRY MESSAGE 3'
        ],
        'restart_messages': [
            'RESTART MESSAGE 1',
            'RESTART MESSAGE 2',
            'RESTART MESSAGE 3'
        ],
        'short_rest_messages': [
            'SHORT REST MESSAGE 1',
            'SHORT REST MESSAGE 2',
            'SHORT REST MESSAGE 3'
        ],
        'long_rest_messages': [
            'LONG REST MESSAGE 1',
            'LONG REST MESSAGE 2',
            'LONG REST MESSAGE 3'
        ],
        'cure_messages': [
            'CURE MESSAGE 1',
            'CURE MESSAGE 2',
            'CURE MESSAGE 3'
        ],
        'mass_cure_messages': [
            'MASS CURE MESSAGE 1',
            'MASS CURE MESSAGE 2',
            'MASS CURE MESSAGE 3'
        ],
        'out_of_time_messages': [
            'OUT OF TIME MESSAGE 1',
            'OUT OF TIME MESSAGE 2',
            'OUT OF TIME MESSAGE 3'
        ],
        'out_of_health_messages': [
            'OUT OF HEALTH MESSAGE 1',
            'OUT OF HEALTH MESSAGE 2',
            'OUT OF HEALTH MESSAGE 3'
        ],
    },
    'tiers': {
        tier: {
            'events': [
                {
                    'id': 'EVENT #%d-%d' % (tier, i+1),
                    'name': 'EVENT #%d-%d' % (tier, i+1),
                    'resources': {
                        'PHYSICAL': {
                            'value': 1 + tier if i % 30 in (0, 1, 2, 15, 16, 17) else 0,
                            'texts': [
                                'ACTION TEXT'
                            ]
                        },
                        'ARCANE': {
                            'value': 1 + tier if i % 30 in (3, 4, 5, 18, 19, 20) else 0,
                            'texts': [
                                'ACTION TEXT'
                            ]
                        },
                        'DIVINE': {
                            'value': 1 + tier if i % 30 in (6, 7, 8, 21, 22, 23) else 0,
                            'texts': [
                                'ACTION TEXT'
                            ]
                        },
                        'STEALTH': {
                            'value': 1 + tier if i % 30 in (9, 10, 11, 24, 25, 26) else 0,
                            'texts': [
                                'ACTION TEXT'
                            ]
                        },
                        'MECHANICAL': {
                            'value': 1 + tier if i % 30 in (12, 13, 14, 27, 28, 29) else 0,
                            'texts': [
                                'ACTION TEXT'
                            ]
                        },
                        'HEALTH': {
                            'value': tier if i % 30 < 15 else 0,
                            'texts': [
                                'ACTION TEXT'
                            ]
                        },
                        'TIME': {
                            'value': tier if i % 30 >= 15 else 0,
                            'texts': [
                                'ACTION TEXT'
                            ]
                        }
                    },
                    'descriptions': [
                        'DESCRIPTION 1',
                        'DESCRIPTION 2',
                        'DESCRIPTION 3'
                    ],
                    'flavor': [
                        'FLAVOR TEXT'
                    ]
                }
                for i in xrange(0, 30)
            ],
            'monsters': [
                {
                    'id': 'MONSTER #%d-%d' % (tier, i+1),
                    'name': 'MONSTER #%d-%d' % (tier, i+1),
                    'resources': {
                        'PHYSICAL': {
                            'value': 1 + tier if i % 20 in (0, 1, 2, 3, 4, 5, 6, 7) else 0,
                            'texts': [
                                'ACTION TEXT'
                            ]
                        },
                        'ARCANE': {
                            'value': 1 + tier if i % 20 in (0, 1, 8, 9, 10, 11, 12, 13) else 0,
                            'texts': [
                                'ACTION TEXT'
                            ]
                        },
                        'DIVINE': {
                            'value': 1 + tier if i % 20 in (2, 3, 8, 9, 14, 15, 16, 17) else 0,
                            'texts': [
                                'ACTION TEXT'
                            ]
                        },
                        'STEALTH': {
                            'value': 1 + tier if i % 20 in (4, 5, 10, 11, 14, 15, 18, 19) else 0,
                            'texts': [
                                'ACTION TEXT'
                            ]
                        },
                        'MECHANICAL': {
                            'value': 1 + tier if i % 20 in (6, 7, 12, 13, 16, 17, 18, 19) else 0,
                            'texts': [
                                'ACTION TEXT'
                            ]
                        },
                        'HEALTH': {
                            'value': 1 + tier,
                            'texts': [
                                'ACTION TEXT'
                            ]
                        }
                    },
                    'loot': 'CONSUMABLE' if i % 2 == 0 else 'EQUIPMENT',
                    'descriptions': [
                        'DESCRIPTION 1',
                        'DESCRIPTION 2',
                        'DESCRIPTION 3'
                    ],
                    'flavor': [
                        'FLAVOR TEXT'
                    ]
                }
                for i in xrange(0, 20)
            ],
            'boss': {
                'id': 'BOSS #%d' % tier,
                'name': 'BOSS #%d' % tier,
                'resources': {
                    'PHYSICAL': {
                        'value': 4 * tier,
                        'texts': [
                            'ACTION TEXT'
                        ]
                    },
                    'ARCANE': {
                        'value': 4 * tier,
                        'texts': [
                            'ACTION TEXT'
                        ]
                    },
                    'DIVINE': {
                        'value': 4 * tier,
                        'texts': [
                            'ACTION TEXT'
                        ]
                    },
                    'STEALTH': {
                        'value': 0,
                        'texts': [
                            'N/A'
                        ]
                    },
                    'MECHANICAL': {
                        'value': 0,
                        'texts': [
                            'N/A'
                        ]
                    },
                    'HEALTH': {
                        'value': 3 * tier,
                        'texts': [
                            'ACTION TEXT'
                        ]
                    }
                },
                'loot': 'WIN' if tier == 5 else 'LEVELUP',
                'descriptions': [
                    'DESCRIPTION 1',
                    'DESCRIPTION 2',
                    'DESCRIPTION 3'
                ],
                'flavor': [
                    'FLAVOR TEXT'
                ]
            },
            'equipment': [
                {
                    'id': 'EQUIPMENT #%d-%d' % (tier, i+1),
                    'name': 'EQUIPMENT #%d-%d' % (tier, i+1),
                    'resources': {
                        'PHYSICAL': {
                            'value': tier if i % 20 in (0, 1, 2, 3) else 0
                        },
                        'ARCANE': {
                            'value': tier if i % 20 in (4, 5, 6, 7) else 0
                        },
                        'DIVINE': {
                            'value': tier if i % 20 in (8, 9, 10, 11) else 0
                        },
                        'STEALTH': {
                            'value': tier if i % 20 in (12, 13, 14, 15) else 0
                        },
                        'MECHANICAL': {
                            'value': tier if i % 20 in (16, 17, 18, 19) else 0
                        }
                    },
                    'descriptions': [
                        'DESCRIPTION 1',
                        'DESCRIPTION 2',
                        'DESCRIPTION 3'
                    ],
                    'flavor': [
                        'FLAVOR TEXT'
                    ]
                }
                for i in xrange(0, 20)
            ],
            'consumables': [
                {
                    'id': 'CONSUMABLE #%d-%d' % (tier, i+1),
                    'name': 'CONSUMABLE #%d-%d' % (tier, i+1),
                    'resources': {
                        'PHYSICAL': {
                            'value': tier if i % 20 in (0, 1, 2) else 0,
                        },
                        'ARCANE': {
                            'value': tier if i % 20 in (3, 4, 5) else 0,
                        },
                        'DIVINE': {
                            'value': tier if i % 20 in (6, 7, 8) else 0,
                        },
                        'STEALTH': {
                            'value': tier if i % 20 in (9, 10, 11) else 0,
                        },
                        'MECHANICAL': {
                            'value': tier if i % 20 in (12, 13, 14) else 0,
                        },
                        'HEALTH': {
                            'value': tier if i % 20 >= 15 else 0
                        }
                    },
                    'descriptions': [
                        'DESCRIPTION 1',
                        'DESCRIPTION 2',
                        'DESCRIPTION 3'
                    ],
                    'flavor': [
                        'FLAVOR TEXT'
                    ]
                }
                for i in xrange(0, 20)
            ]
        }
        for tier in xrange(1, 6)
    },
    'heroes': [
        {
            'id': 'HERO #%d' % (i+1),
            'name': 'HERO #%d' % (i+1),
            'class': 'CLASS PLACEHOLDER',
            'description': 'DESCRIPTION PLACEHOLDER',
            'resources': {
                1: {'PHYSICAL': 5} if i < 10 else {'PHYSICAL': 3, 'STEALTH': 2},
                2: {'PHYSICAL': 6} if i < 10 else {'PHYSICAL': 4, 'STEALTH': 2},
                3: {'PHYSICAL': 7} if i < 10 else {'PHYSICAL': 5, 'STEALTH': 2},
                4: {'PHYSICAL': 8} if i < 10 else {'PHYSICAL': 5, 'STEALTH': 3},
                5: {'PHYSICAL': 9} if i < 10 else {'PHYSICAL': 6, 'STEALTH': 3}
            },
            'discount': {
                'resource': 'PHYSICAL',
                'type': 'EVENT'
            },
            'flavor': 'FLAVOR TEXT',
            'quote': 'QUOTE TEXT'
        }
        for i in xrange(0, 32)
    ]
}

with open('heroes_of_cordan.json', 'w') as json_file:
    json.dump(template, json_file, indent=4, separators=(',', ': '))
